package com.android.mail.browse;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.emailcommon.mail.Address;
import com.android.mail.R;
import com.android.mail.providers.Message;
import com.android.mail.providers.UIProvider;
import com.android.mail.utils.Utils;

public class SpamWarningView extends RelativeLayout implements OnClickListener {
    private ImageView mSpamWarningIcon;
    private TextView mSpamWarningText;
    private TextView mSpamWarningLink;
    private final int mHighWarningColor;
    private final int mLowWarningColor;
    private final int mHighWarningBackgroundColor;
    private final int mLowWarningBackgroundColor;

    public SpamWarningView(Context context) {
        this(context, null);
    }

    public SpamWarningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHighWarningColor = getResources().getColor(R.color.high_spam_color);
        mHighWarningBackgroundColor = getResources().getColor(
            R.color.high_spam_warning_background_color);
        mLowWarningColor = getResources().getColor(R.color.low_spam_color);
        mLowWarningBackgroundColor = getResources().getColor(
            R.color.low_spam_warning_background_color);
    }

    @Override
    public void onFinishInflate() {
        setOnClickListener(this);

        mSpamWarningIcon = (ImageView) findViewById(R.id.spam_warning_icon);
        mSpamWarningText = (TextView) findViewById(R.id.spam_warning_text);
        mSpamWarningLink = (TextView) findViewById(R.id.spam_warning_link);
        mSpamWarningLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.spam_warning) {
            // Do nothing (TODO?)
        } else if (id == R.id.spam_warning_link) {
            // TODO - once we have final design,
            // make clicking this text do what needs to be done
        }

    }

    public void showSpamWarning(Message message, Address sender) {
        setVisibility(VISIBLE);

        // Sets the text and adds any necessary formatting
        // to enable the proper display.
        final String senderAddress = sender.getAddress();
        final String senderDomain = senderAddress.substring(senderAddress.indexOf('@')+1);
        mSpamWarningText.setText(Utils.convertHtmlToPlainText(String.format(
                message.spamWarningString, senderAddress, senderDomain)));

        if (message.spamWarningLevel == UIProvider.SpamWarningLevel.HIGH_WARNING) {
            setBackgroundColor(mHighWarningBackgroundColor);
            mSpamWarningText.setTextColor(mHighWarningColor);
            mSpamWarningIcon.setImageResource(R.drawable.ic_alert_grey);
        } else {
            setBackgroundColor(mLowWarningBackgroundColor);
            mSpamWarningText.setTextColor(mLowWarningColor);
            mSpamWarningIcon.setImageResource(R.drawable.ic_alert_red);
        }

        // Sets the link to the appropriate text
        // and sets visibility, if necessary.
        final int linkType = message.spamLinkType;
        switch (linkType) {
            case UIProvider.SpamWarningLinkType.NO_LINK:
                mSpamWarningLink.setVisibility(GONE);
                break;
            case UIProvider.SpamWarningLinkType.IGNORE_WARNING:
                mSpamWarningLink.setVisibility(VISIBLE);
                mSpamWarningLink.setText(R.string.ignore_spam_warning);
                break;
            case UIProvider.SpamWarningLinkType.REPORT_PHISHING:
                mSpamWarningLink.setVisibility(VISIBLE);
                mSpamWarningLink.setText(R.string.report_phishing);
                break;
        }
    }
}
