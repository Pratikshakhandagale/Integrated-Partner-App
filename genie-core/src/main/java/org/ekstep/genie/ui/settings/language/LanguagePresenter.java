package org.ekstep.genie.ui.settings.language;

import android.app.Activity;
import android.content.Context;

import org.ekstep.genie.R;
import org.ekstep.genie.asynctask.ChangeLanguageAsynTask;
import org.ekstep.genie.base.BaseView;
import org.ekstep.genie.telemetry.EnvironmentId;
import org.ekstep.genie.telemetry.TelemetryAction;
import org.ekstep.genie.telemetry.TelemetryBuilder;
import org.ekstep.genie.telemetry.TelemetryHandler;
import org.ekstep.genie.telemetry.TelemetryStageId;
import org.ekstep.genie.util.FontUtil;
import org.ekstep.genie.util.ShowProgressDialog;
import org.ekstep.genie.util.preference.PreferenceUtil;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;

/**
 * Created by Sneha on 9/17/2017.
 */

public class LanguagePresenter implements LanguageContract.Presenter {
    private Context mContext;
    private LanguageContract.View mLanguageView;

    @Override
    public void bindView(BaseView view, Context context) {
        mContext = context;
        mLanguageView = (LanguageContract.View) view;
    }

    @Override
    public void unbindView() {
        mContext = null;
        mLanguageView = null;
    }

    @Override
    public void handleLanguageChange(int index) {
        String locale = FontUtil.getInstance().getLocaleAtPosition(index);
        if (PreferenceUtil.isFirstTime().equalsIgnoreCase("false")) {
            ChangeLanguageAsynTask changeLanguageAsyncTask = new ChangeLanguageAsynTask((Activity) mContext);
            changeLanguageAsyncTask.execute(locale);
        } else {
            PreferenceUtil.setLanguageSelected(true);
            PreferenceUtil.setLanguage(locale);
            FontUtil.getInstance().changeLocale();
            ShowProgressDialog.showProgressDialog(mContext, mContext.getString(R.string.msg_setting_changing_language));
            TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(EnvironmentId.HOME, InteractionType.OTHER, TelemetryAction.LANGUAGE_SETTINGS_SUCCESS, TelemetryStageId.SETTINGS_LANGUAGE, locale, null));
        }
    }
}
