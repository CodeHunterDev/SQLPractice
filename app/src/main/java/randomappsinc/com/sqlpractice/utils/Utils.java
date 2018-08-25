package randomappsinc.com.sqlpractice.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.database.QuestionServer;

public class Utils {

    public static void showSnackbar(View parent, String content) {
        Context context = parent.getContext();
        Snackbar snackbar = Snackbar.make(parent, content, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.app_turquoise));
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();
    }

    public static void showLongSnackbar(View parent, String content) {
        Context context = parent.getContext();
        final Snackbar snackbar = Snackbar.make(parent, content, Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.app_turquoise));
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setMaxLines(8);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    // For the choose multiple names at once case. We're just generating indices
    public static int getRandomQuestionIndex(int currentQuestionIndex) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < QuestionServer.getNumQuestions(); i++) {
            if (i != currentQuestionIndex) {
                list.add(i);
            }
        }
        Collections.shuffle(list);
        return list.get(0);
    }

    public static void copyTextToClipboard(String text, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            return;
        }
        ClipData clip = ClipData.newPlainText(context.getString(R.string.answer_query), text);
        clipboard.setPrimaryClip(clip);
    }

    public static void loadMenuIcon(Menu menu, int itemId, Icon icon, Context context) {
        menu.findItem(itemId).setIcon(
                new IconDrawable(context, icon)
                        .colorRes(R.color.white)
                        .actionBarSize());
    }
}