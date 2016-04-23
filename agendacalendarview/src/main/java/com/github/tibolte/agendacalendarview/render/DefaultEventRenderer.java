package com.github.tibolte.agendacalendarview.render;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
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
        ImageView taskImage = (ImageView) view.findViewById(R.id.task_image);
        ImageView arrowImage = (ImageView) view.findViewById(R.id.right_arrow_image);
        TextView txtDate = (TextView) view.findViewById(R.id.task_date);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent1);
        View priority = view.findViewById(R.id.priority_bar);


        txtTitle.setSelected(true);
        txtTitle.setText(event.getTitle());

        Drawable shapeDrawable = priority.getBackground();
        GradientDrawable colorDrawable = (GradientDrawable) shapeDrawable;
        colorDrawable.setColor(event.getColor());
        priority.setBackground(shapeDrawable);

        txtProject.setText(event.getDescription());
        txtComment.setText(event.getmCommentCount()+ " " + view.getResources().getString(R.string.task_comment));
        txtUser.setText(Integer.toString(event.getmUserCount())+ " " + view.getResources().getString(R.string.task_user));
        txtDate.setText(event.getEndDate());
//        if (event.==100){
//            holder.taskImage.setImageResource(R.drawable.ic_notifications_green_24dp);
//        }
//        else {
        int themeType = AgendaCalendarView.db.getInt("ThemeType");
        if(themeType == 1) {
                taskImage.setImageResource(R.drawable.task_progress_blue);
                arrowImage.setImageResource(R.drawable.circle_right_blue);
            }else{
                taskImage.setImageResource(R.drawable.task_progress_black);
                arrowImage.setImageResource(R.drawable.circle_right);
            }
//        }
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.algorepublic.zoho.Event_Click");
                intent.putExtra("position", event.getPosition());
                intent.putExtra("Action","Detail");
                view.getContext().sendBroadcast(intent);
            }
        });
        view.findViewById(R.id.btDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.algorepublic.zoho.Event_Click");
                intent.putExtra("position", event.getPosition());
                intent.putExtra("Action","Delete");
                view.getContext().sendBroadcast(intent);
            }
        });

        view.findViewById(R.id.btEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.algorepublic.zoho.Event_Click");
                intent.putExtra("position", event.getPosition());
                intent.putExtra("Action","Edit");
                view.getContext().sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getEventLayout() {
        return R.layout.view_agenda_event;
    }



    // endregion
}
