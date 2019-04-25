package com.sadek.se7tak.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.sadek.se7tak.R;


public class LanguageDialog extends Dialog {
    languageDialogAction languageDialogAction;

    Button arabicBtn,englishBtn;
    public LanguageDialog(@NonNull final Context context, final languageDialogAction languageDialogAction) {
        super(context);
        setContentView(R.layout.language_custom_dialog);
        this.languageDialogAction=languageDialogAction;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        arabicBtn=findViewById(R.id.arabic_btn);
        englishBtn=findViewById(R.id.english_btn);
        arabicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialogAction.onGetCode(true);
            }
        });
        englishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageDialogAction.onGetCode(false);
            }
        });
    }

    public interface languageDialogAction{
        void onGetCode(boolean langStatus);
    }
}