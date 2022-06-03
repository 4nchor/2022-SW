package com.libienz.se_2022_closet.startApp_1.ootd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.libienz.se_2022_closet.R;
import com.libienz.se_2022_closet.startApp_1.util.FirebaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<Date> dayList;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    public CalendarAdapter(ArrayList<Date> dayList) {
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calender_cell, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        //날짜 변수에 담기
        Date monthDate = dayList.get(position);

        //달력 초기화
        Calendar dateCalendar = Calendar.getInstance();

        dateCalendar.setTime(monthDate);

        //현재 년 월
        int currentDay = CalenderUtil.selectedDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = CalenderUtil.selectedDate.get(Calendar.MONTH)+1;
        int currentYear = CalenderUtil.selectedDate.get(Calendar.YEAR);

        //넘어온 데이터
        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);

        String throwDate = displayMonth+"월 "+displayDay+"일";
        String dateKey =displayYear+"year"+ displayMonth+"month"+displayDay+"day";

        String uidString =auth.getCurrentUser().getUid().toString();
        try {
            FirebaseReference.reference.child("board").child(uidString).child(dateKey+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    holder.parentView.setBackgroundColor(Color.parseColor("#7087FF"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("이미지확인","저장된 내용이없겠지");
                    //Toast.makeText(holder.parentView.getContext(), "저장된 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //비교해서 년, 월 같으면 검정색 다르면 회색으로 표시
        if(displayMonth == currentMonth && displayYear == currentYear){
            holder.dayText.setTextColor(Color.BLACK);
            //주말색상체크, 만약 토요일이면 파랑, 일요일이면 빨강으로 변환
            if( (position + 1) % 7 == 0){ //토요일 파랑

                holder.dayText.setTextColor(Color.BLUE);

            }else if( position == 0 || position % 7 == 0){ //일요일 빨강

                holder.dayText.setTextColor(Color.RED);
            }
        }else{
            holder.dayText.setTextColor(Color.parseColor("#E2E2E2"));
        }

        //날짜 변수에 담기
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);

        holder.dayText.setText(String.valueOf(dayNo));

        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            //클릭 시 기능
            public void onClick(View view) {
                String testtoastday = displayYear+"년"+displayMonth+"월"+displayDay+"일";
                Toast.makeText(holder.itemView.getContext(),testtoastday,Toast.LENGTH_SHORT).show();

                //클릭 시 BoardActivity로 이동
                Context context = view.getContext();
                Intent selectDay = new Intent(context, BoardActivity.class);

                //넘기고 싶은 데이터
                selectDay .putExtra("boardDate" ,throwDate);
                selectDay .putExtra("dateKey" ,dateKey);
                context.startActivity(selectDay);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView dayText;

        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            dayText = itemView.findViewById(R.id.dayText);

            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}