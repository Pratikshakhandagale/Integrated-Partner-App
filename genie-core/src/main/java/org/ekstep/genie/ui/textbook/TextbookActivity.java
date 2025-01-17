package org.ekstep.genie.ui.textbook;

import android.os.Bundle;

import org.ekstep.genie.R;
import org.ekstep.genie.activity.BaseActivity;
import org.ekstep.genie.base.BaseView;
import org.ekstep.genie.base.IPresenterFactory;
import org.ekstep.genie.model.TextbookSection;
import org.ekstep.genie.util.Constant;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentData;

import java.util.List;

/**
 * Created on 8/2/17.
 *
 * @author shriharsh
 */
public class TextbookActivity extends BaseActivity {
    private TextbookFragment textbooksFragment;
    private List<TextbookSection> textbookSectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collections);

        textbooksFragment = new TextbookFragment();
        textbooksFragment.setArguments(getIntent().getExtras());
        setFragment(textbooksFragment, false);
    }

    @Override
    protected IPresenterFactory getPresenterFactory() {
        return null;
    }

    @Override
    protected String getTAG() {
        return null;
    }

    @Override
    protected BaseView getBaseView() {
        return null;
    }

    /**
     * This method will help to get textBookSectionList
     *
     * @return Textbook section list.
     */
    public List<TextbookSection> getTextbookSectionList() {
        return textbookSectionList;
    }

    /**
     * This method will set the textBookSectionList
     *
     * @param textbookSectionList Textbook section list.
     */
    public void setTextbookSectionList(List<TextbookSection> textbookSectionList) {
        this.textbookSectionList = textbookSectionList;
    }

    @Override
    public void onBackPressed() {
        if (textbooksFragment != null && textbooksFragment.isVisible()) {
           textbooksFragment.handleTelemetryEndEvent();
        }
        super.onBackPressed();
    }
}
