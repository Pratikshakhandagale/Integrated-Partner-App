package org.ekstep.genie.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import org.ekstep.genie.ui.splash.IStartUp;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created on 5/5/2016.
 *
 * @author swayangjit_gwl
 */
public class DeepLinkNavigation {

    public static final String DEEPLINK_EXPLORE_CONTENT_IDENTIFIER = "landing";
    private static final String TAG = "DeepLinkNavigation";
    private static final String DEEPLINK_SEARCH_RESULT_IDENTIFIER = "l";
    private static final String TAG_NAME = "k";
    private static final String TAG_DESCRIPTION = "d";
    private static final String TAG_SDATE = "s";
    private static final String TAG_ENDDATE = "e";
    private static final String DEEPLINK_SORT_QUERY = "sort";
    private static final String DEEPLINK_TYPE = "type";

    private Activity mActivity;

    private IStartUp mIStartUp;

    private Intent mIntent;

    public DeepLinkNavigation(Activity activity) {
        mActivity = activity;
    }

    private HashMap<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        HashMap<String, String> query_pairs = null;
        String query = url.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            query_pairs = new LinkedHashMap<>();

            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }

        return query_pairs;
    }

    public void validateAndHandleDeepLink(Intent intent, IValidateDeepLink iValidateDeepLink, IStartUp startBehaviour) {
        mIStartUp = startBehaviour;
        mIntent = intent;
        if (intent != null && intent.getData() != null) {
            validateAndHandleDeepLink(intent.getData(), iValidateDeepLink);
        } else {
            // Not a valid deep link
            if (iValidateDeepLink != null) {
                iValidateDeepLink.notAValidDeepLink();
            }
        }
    }

    public void validateAndHandleDeepLink(Uri intentData, IValidateDeepLink iValidateDeepLink) {
        if (intentData != null) {
            if (DeepLinkUtility.isDeepLink(intentData)) { // If deeplink clicked from content's last page.
                if (iValidateDeepLink != null) {
                    iValidateDeepLink.validLocalDeepLink();
                }
                if (DeepLinkUtility.isDeepLinkSetTag(intentData)) {
                    iValidateDeepLink.onTagDeeplinkFound(intentData.getQueryParameter(TAG_NAME), intentData.getQueryParameter(TAG_DESCRIPTION), intentData.getQueryParameter(TAG_SDATE), intentData.getQueryParameter(TAG_ENDDATE));
                } else {
                    handleLocalDeepLink(intentData);
                }


            } else if ((DeepLinkUtility.isDeepLinkHttp(intentData)
                    || DeepLinkUtility.isDeepLinkHttps(intentData))
                    && !new File(intentData.toString()).exists()) {    // Server deep link
                if (iValidateDeepLink != null) {
                    iValidateDeepLink.validServerDeepLink();
                }

                handleServerDeepLink(intentData.toString(), iValidateDeepLink);
            } else {
                // Not a valid deep link
                if (iValidateDeepLink != null) {
                    iValidateDeepLink.notAValidDeepLink();
                }
            }
        } else {
            // Not a valid deep link
            if (iValidateDeepLink != null) {
                iValidateDeepLink.notAValidDeepLink();
            }
        }
    }

    private void handleLocalDeepLink(Uri intentData) {
        if (mIStartUp != null) {
            mIStartUp.handleLocalDeepLink(mIntent);
        }
    }


    private void handleServerDeepLink(String url, IValidateDeepLink iValidateDeepLink) {
        try {
            HashMap<String, String> parameters = splitQuery(new URL(url));
            if (parameters != null) {
                if (parameters.get(TAG_NAME) != null) {
                    if (iValidateDeepLink != null) {
                        iValidateDeepLink.onTagDeeplinkFound(parameters.get(TAG_NAME), parameters.get(TAG_DESCRIPTION), parameters.get(TAG_SDATE), parameters.get(TAG_ENDDATE));
                    }
                }
            } else {
                if (mIStartUp != null) {
                    mIStartUp.handleServerDeepLink(mIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface IValidateDeepLink {
        void validLocalDeepLink();

        void validServerDeepLink();

        void notAValidDeepLink();

        void onTagDeeplinkFound(String tagName, String description, String startDate, String endDate);
    }
}
