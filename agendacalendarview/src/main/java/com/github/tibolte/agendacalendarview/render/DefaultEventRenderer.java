package com.github.tibolte.agendacalendarview.render;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.R;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;

/**
 * Class helping to inflate our default layout in the AgendaAdapter
 */
public class DefaultEventRenderer extends EventRenderer<BaseCalendarEvent> {

    // region class - EventRenderer

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void render(@NonNull View view, @NonNull BaseCalendarEvent event) {
        TextView txtTitle = (TextView) view.findViewById(R.id.task_name);
        TextView txtProject = (TextView) view.findViewById(R.id.project_name);
        TextView txtComment = (TextView) view.findViewById(R.id.task_comment);
        TextView txtUser = (TextView) view.findViewById(R.id.task_users);
        TextView txtDate = (TextView) view.findViewById(R.id.task_date);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent1);
        View priority = view.findViewById(R.id.priority_bar);


        txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));

        txtTitle.setText(event.getTitle());

        Drawable shapeDrawable = priority.getBackground();
        GradientDrawable colorDrawable = (GradientDrawable) shapeDrawable;
        colorDrawable.setColor(event.getColor());
        priority.setBackground(shapeDrawable);

        txtTitle.setTextColor(view.getResources().getColor(android.R.color.black));
        txtProject.setText(event.getDescription());
        txtComment.setText(event.getLocation()+ " " + view.getResources().getString(R.string.task_comment));
        txtUser.setText(Integer.toString(event.getmUserCount())+ " " + view.getResources().getString(R.string.task_user));
        txtDate.setText(event.getEndDate());

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LOG_TAG", "String.format(");
            }
        });
    }

    @Override
    public int getEventLayout() {
        return R.layout.view_agenda_event;
    }

    // endregion
}
