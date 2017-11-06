package org.ekstep.ipa.ui.landing.geniechildren;

import android.content.Context;
import android.text.TextUtils;

import org.ekstep.genie.base.BaseView;
import org.ekstep.genie.util.LogUtil;
import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.async.UserService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.ipa.PartnerApp;
import org.ekstep.ipa.adapter.ChildrenAdapter;
import org.ekstep.ipa.db.StudentDAO;
import org.ekstep.ipa.model.Partner;
import org.ekstep.ipa.model.Student;


import java.util.HashMap;
import java.util.List;

/**
 * @author vinayagasundar
 */

public class GenieChildrenPresenter implements GenieChildrenContract.Presenter,
        ChildrenAdapter.OnSelectedChildListener {

    private static final String TAG = "GenieChildrenPresenter";

    private GenieChildrenContract.View mGenieChildView;

    private Student mStudent = null;

    private UserService mUserService;

    @Override
    public void getAllGenieChildren() {
        mGenieChildView.showLoader();

        List<Student> studentList = StudentDAO.getInstance().getAllGenieStudent();

        if (studentList != null && studentList.size() > 0) {
            mGenieChildView.setGenieChildListAdapter(studentList, this);
            mGenieChildView.hideLoader();
            mGenieChildView.hideEmptyView();
        } else {
            mGenieChildView.showEmptyView();
        }

    }

    @Override
    public void launchGenieForSelectedStudent() {
        if (mStudent == null) {
            return;
        }

        setCurrentStudent(false);
    }



    @Override
    public void bindView(BaseView view, Context context) {
        mGenieChildView = (GenieChildrenContract.View) view;
    }

    @Override
    public void unbindView() {
        mGenieChildView = null;
    }


    @Override
    public void onChildSelected(Student student) {
        mStudent = student;
        mGenieChildView.enableBrowseLesson();
    }

    @Override
    public void onChildUnSelected(Student student) {
        mStudent = null;
        mGenieChildView.disableBrowseLesson();
    }

    private void setCurrentStudent(final boolean isCreateChild) {
        mUserService = GenieService.getAsyncService().getUserService();
        mUserService.setCurrentUser(mStudent.getUid(), new IResponseHandler<Void>() {
            @Override
            public void onSuccess(GenieResponse<Void> genieResponse) {
                sendPrivateData(mStudent, isCreateChild);
            }

            @Override
            public void onError(GenieResponse<Void> genieResponse) {
                LogUtil.e(TAG, GsonUtil.toJson(genieResponse));

                if (genieResponse != null && genieResponse.getError() != null
                        && genieResponse.getError().equals("INVALID_USER")) {
                    createUserProfile();
                }
            }
        });
    }

    private void createUserProfile() {
        String handle = mStudent.getName();
        String gender = mStudent.getGender();
        Profile profile = new Profile(handle, "Avatar","en");

        if (!TextUtils.isEmpty(gender)) {
            profile.setGender(gender);
        }

        if (mUserService == null) {
            mUserService = GenieService.getAsyncService().getUserService();
        }

        mUserService.createUserProfile(profile, new IResponseHandler<Profile>() {
            @Override
            public void onSuccess(GenieResponse<Profile> genieResponse) {
                String uid = genieResponse.getResult().getUid();
                mStudent.setUid(uid);
                StudentDAO.getInstance().updateStudentUID(mStudent.getStudentId(), uid);

                setCurrentStudent(true);
            }

            @Override
            public void onError(GenieResponse<Profile> genieResponse) {
                LogUtil.e(TAG, GsonUtil.toJson(genieResponse));
            }
        });
    }

    private void sendPrivateData(Student student, boolean isCreateStudent) {
        Partner partner = PartnerApp.getPartnerApp().getPartnerConfig().getPartner();

        String privateData;

        if (isCreateStudent) {
            privateData = GsonUtil.toJson(student);
        } else {
            HashMap<String, String> data = new HashMap<>();

            data.put(StudentDAO.COLUMN_STUDENT_ID, student.getStudentId());
            data.put(StudentDAO.COLUMN_UID, student.getUid());

            privateData = GsonUtil.toJson(data);
        }

        PartnerData partnerData = new PartnerData(null, null, partner.getId(), privateData,
                partner.getPublicKey());

        GenieService.getAsyncService()
                .getPartnerService().sendData(partnerData, new IResponseHandler<String>() {
            @Override
            public void onSuccess(GenieResponse<String> genieResponse) {
                LogUtil.i(TAG, GsonUtil.toJson(genieResponse));
                mGenieChildView.showGenieHomeScreen();
            }

            @Override
            public void onError(GenieResponse<String> genieResponse) {
                LogUtil.i(TAG, GsonUtil.toJson(genieResponse));
            }
        });
    }
}
