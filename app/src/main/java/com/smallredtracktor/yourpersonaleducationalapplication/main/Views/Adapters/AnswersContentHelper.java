package com.smallredtracktor.yourpersonaleducationalapplication.main.Views.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.LinearLayout;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;

import java.util.ArrayList;

public class AnswersContentHelper {

    private final Context context;
    private final LinearLayout answersLayout;
    private final int layoutHeight;
    private ArrayList<String> answersId;
    private ICreateTestFragmentMVPprovider.IPresenter presenter;

    public AnswersContentHelper(Context context, ICreateTestFragmentMVPprovider.IPresenter createTestFragmentPresenter, LinearLayout answersLayout) {
        this.context = context;
        this.answersLayout = answersLayout;
        this.layoutHeight = answersLayout.getLayoutParams().height;
        this.presenter = createTestFragmentPresenter;
        answersId = new ArrayList<>();
    }

    public void addFirstItem() {
        answersId.add("new");
        Button item = makeItem("new");
        answersLayout.addView(item, 0);
    }

    private Button makeItem(String msg)
    {
        Button button = new Button(context);
        button.setLayoutParams(new LinearLayout.LayoutParams(700,layoutHeight));
        button.setText(msg);
        button.setOnClickListener(v ->
                presenter.onAnswerTextItemInteraction(msg));
        return button;
    }

    private Button makeItem(String id, int type, String param) {
        Button view = new Button(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(700,layoutHeight));
        switch (type)
        {
            case 0 :
            {
                view.setText(param);
                view.setOnClickListener(v -> presenter.onAnswerTextItemInteraction(id));
                break;
            }

            case 1 :
            {
                if (param == null)
                {
                    view.setText("loading");
                } else {
                    view.setBackgroundDrawable(Drawable.createFromPath(param));

                }
                break;

            }

            case 2 :
            {
                if (param == null)
                {
                    view.setText("loading");
                } else {
                    view.setBackgroundDrawable(Drawable.createFromPath(param));
                }
                break;
            }

            case 3 :
            {
                if (param == null)
                {
                    view.setText("loading");
                } else {
                    view.setText(param);
                }
                view.setOnClickListener(v -> presenter.onAnswerTextItemInteraction(id));
                break;
            }
        }

        return view;
    }

    public void setItem(String id, int type, String param) {
        int position;
        if(answersId.contains(id)){
            position = answersId.indexOf(id);
        } else
        {
            position = answersId.indexOf("new");
        }
        Button item = makeItem(id,type,param);
        answersLayout.removeViewAt(position);
        answersLayout.addView(item, position);
        answersId.set(position,id);
    }

    public void addItem(String id, String param) {
        answersId.add(id);
        Button item = makeItem(param);
        answersLayout.addView(item, answersId.size() - 1);
    }

    public void removeItem(String id) {
        answersLayout.removeViewAt(answersId.indexOf(id));
        answersId.remove(id);
    }

}
