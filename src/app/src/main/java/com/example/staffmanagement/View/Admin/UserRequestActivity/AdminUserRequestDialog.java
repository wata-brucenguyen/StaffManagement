package com.example.staffmanagement.View.Admin.UserRequestActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.staffmanagement.R;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.staffmanagement.View.Data.AdminRequestFilter.SORT.None;

public class AdminUserRequestDialog extends DialogFragment{
    public enum STATE {
        Waiting, Decline, Accept;
    }
    public enum TYPE_TIME_FILTER{
        FROM, TO
    }
    public enum TYPE_CHOOSE_TIME_FILTER{
        DAY, HOUR
    }

    private CheckBox cbWaiting, cbAccept, cbDecline;
    private RadioButton rbSortNone, rbSortTitle, rbSortDateTime, rbSortTitleAsc, rbSortDateTimeAsc, rbSortTitleDesc, rbSortDateTimeDesc;
    private RadioGroup rgParent, rgSortTitle, rgSortDateTime;
    private AdminRequestFilter mFilter;
    private UserRequestInterface mInterface;
    private static EditText edtDayFrom;
    private static EditText edtDayTo;
    private static EditText edtHourFrom;
    private static EditText edtHourTo;
    private ImageView imvDayFrom, imvDayTo, imvHourFrom, imvHourTo;
    private TextView txtClose, txtAccept, txtReset;
    protected static AdminUserRequestDialog.TYPE_TIME_FILTER typeTimeFilter;
    protected static AdminUserRequestDialog.TYPE_CHOOSE_TIME_FILTER typeChooseTimeFilter;

    public AdminUserRequestDialog(AdminRequestFilter mFilter, UserRequestInterface mInterface) {
        this.mFilter = mFilter;
        this.mInterface = mInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_admin_request_filter, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapping(view);
        eventRegister();
        setEnableRadioButton(false);
        prepareData();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void mapping(View view) {
        cbWaiting = view.findViewById(R.id.checkBox_filter_state_waiting);
        cbAccept = view.findViewById(R.id.checkBox_filter_state_accept);
        cbDecline = view.findViewById(R.id.checkBox_filter_state_decline);

        rbSortNone = view.findViewById(R.id.radioButton_sort_none_filter);
        rbSortTitle = view.findViewById(R.id.radioButton_sort_title_filter);
        rbSortDateTime = view.findViewById(R.id.radioButton_sort_dateTime_filter);

        rbSortTitleAsc = view.findViewById(R.id.radioButton_sort_title_asc);
        rbSortTitleDesc = view.findViewById(R.id.radioButton_sort_title_desc);
        rbSortDateTimeAsc = view.findViewById(R.id.radioButton_sort_dateTime_asc);
        rbSortDateTimeDesc = view.findViewById(R.id.radioButton_sort_dateTime_desc);

        rgParent = view.findViewById(R.id.radioGroup_parent);
        rgSortTitle = view.findViewById(R.id.radioGroup_sort_title_filter);
        rgSortDateTime = view.findViewById(R.id.radioGroup_sort_dateTime_filter);

        txtClose = view.findViewById(R.id.textView_close_filter);
        txtAccept = view.findViewById(R.id.textView_accept_filter);
        txtReset = view.findViewById(R.id.textView_reset_filter);

        edtDayFrom = view.findViewById(R.id.editText_filter_day_from);
        edtDayTo = view.findViewById(R.id.editText_filter_day_to);
        edtHourFrom = view.findViewById(R.id.editText_filter_hour_from);
        edtHourTo = view.findViewById(R.id.editText_filter_hour_to);

        imvDayFrom = view.findViewById(R.id.imageView_choose_day_from);
        imvDayTo = view.findViewById(R.id.imageView_choose_day_to);
        imvHourFrom = view.findViewById(R.id.imageView_choose_hour_from);
        imvHourTo = view.findViewById(R.id.imageView_choose_hour_to);
    }

    private void prepareData(){
        for (AdminRequestFilter.STATE s : mFilter.getStateList()) {
            if (s.equals(AdminRequestFilter.STATE.Waiting))
                cbWaiting.setChecked(true);
            else if (s.equals(AdminRequestFilter.STATE.Accept))
                cbAccept.setChecked(true);
            else if (s.equals(AdminRequestFilter.STATE.Decline))
                cbDecline.setChecked(true);
        }

        switch (mFilter.getSortName()){
            case None:
                break;
            case Title:
                setEnableRbTitle(true);
                rbSortTitle.setChecked(true);
                if(mFilter.getSortType().equals(AdminRequestFilter.SORT_TYPE.ASC))
                    rbSortTitleAsc.setChecked(true);
                else
                    rbSortTitleDesc.setChecked(true);
                break;
            case DateTime:
                setEnableRbDateTime(true);
                rbSortDateTime.setChecked(true);
                if(mFilter.getSortType().equals(AdminRequestFilter.SORT_TYPE.ASC))
                    rbSortDateTimeAsc.setChecked(true);
                else
                    rbSortDateTimeDesc.setChecked(true);
                break;
        }

        if (mFilter.getFromDateTime() != 0 && mFilter.getToDateTime() != 0) {

            String []dayFromArr = GeneralFunc.convertMilliSecToDateString(mFilter.getFromDateTime()).split(" ");
            String []dayToArr = GeneralFunc.convertMilliSecToDateString(mFilter.getToDateTime()).split(" ");
            edtDayFrom.setText(dayFromArr[0]);
            edtHourFrom.setText(dayFromArr[1]);
            edtDayTo.setText(dayToArr[0]);
            edtHourTo.setText(dayToArr[1]);
        }
    }

    private void setEnableRadioButton(boolean b) {
        setEnableRbTitle(b);
        setEnableRbDateTime(b);
    }

    private void setCheckedRadioButton(boolean b) {
        setCheckedRbTitle(b);
        setCheckedRbDateTime(b);
    }

    private void setEnableRbTitle(boolean b) {
        rbSortTitleAsc.setEnabled(b);
        rbSortTitleDesc.setEnabled(b);
    }

    private void setEnableRbDateTime(boolean b) {
        rbSortDateTimeAsc.setEnabled(b);
        rbSortDateTimeDesc.setEnabled(b);
    }

    private void setCheckedRbTitle(boolean b) {
        rbSortTitleAsc.setChecked(b);
        rbSortTitleDesc.setChecked(b);
    }

    private void setCheckedRbDateTime(boolean b) {
        rbSortDateTimeAsc.setChecked(b);
        rbSortDateTimeDesc.setChecked(b);
    }

    private void eventRegister() {
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilter.setToDateTime(0);
                mFilter.setFromDateTime(0);
                mFilter.setSortName(None);
                mFilter.setSortType(AdminRequestFilter.SORT_TYPE.None);
                mFilter.setStateList(new ArrayList<AdminRequestFilter.STATE>());

                setEnableRadioButton(false);
                rbSortNone.setChecked(true);
                cbWaiting.setChecked(false);
                cbAccept.setChecked(false);
                cbDecline.setChecked(false);

                edtHourFrom.setText("");
                edtHourTo.setText("");
                edtDayFrom.setText("");
                edtDayTo.setText("");
            }
        });

        rgParent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int idGroup = radioGroup.getCheckedRadioButtonId();
                switch (idGroup) {
                    case R.id.radioButton_sort_none_filter:
                        setCheckedRadioButton(false);
                        setEnableRadioButton(false);
                        break;
                    case R.id.radioButton_sort_title_filter:
                        setEnableRbTitle(true);
                        rbSortTitleAsc.setChecked(true);
                        if(!rbSortTitleAsc.isChecked())
                            rbSortTitleDesc.setChecked(true);
                        setCheckedRbDateTime(false);
                        setEnableRbDateTime(false);
                        break;
                    case R.id.radioButton_sort_dateTime_filter:
                        setEnableRbDateTime(true);
                        rbSortDateTimeAsc.setChecked(true);
                        if(!rbSortDateTimeAsc.isChecked())
                            rbSortDateTimeDesc.setChecked(true);
                        setCheckedRbTitle(false);
                        setEnableRbTitle(false);
                        break;
                }
            }
        });

        txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbWaiting.isChecked() && !mFilter.getStateList().contains(AdminRequestFilter.STATE.Waiting))
                    mFilter.getStateList().add(AdminRequestFilter.STATE.Waiting);
                else if (!cbWaiting.isChecked() && mFilter.getStateList().contains(AdminRequestFilter.STATE.Waiting))
                    mFilter.getStateList().remove(AdminRequestFilter.STATE.Waiting);

                if (cbAccept.isChecked() && !mFilter.getStateList().contains(AdminRequestFilter.STATE.Accept))
                    mFilter.getStateList().add(AdminRequestFilter.STATE.Accept);
                else if (!cbAccept.isChecked() && mFilter.getStateList().contains(AdminRequestFilter.STATE.Accept))
                    mFilter.getStateList().remove(AdminRequestFilter.STATE.Accept);

                if (cbDecline.isChecked() && !mFilter.getStateList().contains(AdminRequestFilter.STATE.Decline))
                    mFilter.getStateList().add(AdminRequestFilter.STATE.Decline);
                else if (!cbDecline.isChecked() && mFilter.getStateList().contains(AdminRequestFilter.STATE.Decline))
                    mFilter.getStateList().remove(AdminRequestFilter.STATE.Decline);

                int idParentRaGr = rgParent.getCheckedRadioButtonId();
                switch (idParentRaGr) {
                    case R.id.radioButton_sort_none_filter:
                        mFilter.setSortName(None);
                        mFilter.setSortType(AdminRequestFilter.SORT_TYPE.None);
                        break;
                    case R.id.radioButton_sort_title_filter:
                        mFilter.setSortName(AdminRequestFilter.SORT.Title);
                        if (rbSortTitleAsc.isChecked())
                            mFilter.setSortType(AdminRequestFilter.SORT_TYPE.ASC);
                        else
                            mFilter.setSortType(AdminRequestFilter.SORT_TYPE.DESC);

                        break;
                    case R.id.radioButton_sort_dateTime_filter:
                        mFilter.setSortName(AdminRequestFilter.SORT.DateTime);
                        if (rbSortDateTimeAsc.isChecked())
                            mFilter.setSortType(AdminRequestFilter.SORT_TYPE.ASC);
                        else
                            mFilter.setSortType(AdminRequestFilter.SORT_TYPE.DESC);
                        break;
                }

                String dayFrom = edtDayFrom.getText().toString();
                String dayTo = edtDayTo.getText().toString();
                String hourFrom = edtHourFrom.getText().toString();
                String hourTo = edtHourTo.getText().toString();
                if( !TextUtils.isEmpty(dayFrom) || !TextUtils.isEmpty(dayTo) || !TextUtils.isEmpty(hourFrom) || !TextUtils.isEmpty(hourTo)){

                    if(TextUtils.isEmpty(dayFrom)){
                        Toast.makeText(getActivity(),"Day from is empty",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"Your must choose entire day and time or not choose any day time",Toast.LENGTH_SHORT).show();
                        return;
                    } else if(TextUtils.isEmpty(dayTo)){
                        Toast.makeText(getActivity(),"Day to is empty",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"Your must choose entire day and time or not choose any day time",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(TextUtils.isEmpty(hourFrom)){
                        Toast.makeText(getActivity(),"Hour from is empty",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"Your must choose entire day and time or not choose any day time",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(TextUtils.isEmpty(hourTo)){
                        Toast.makeText(getActivity(),"Hour to is empty",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"Your must choose entire day and time or not choose any day time",Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if( !TextUtils.isEmpty(dayFrom) && !TextUtils.isEmpty(dayTo) && !TextUtils.isEmpty(hourFrom) && !TextUtils.isEmpty(hourTo)){
                    long dateFromLong = GeneralFunc.convertDateStringToLong(dayFrom+" "+hourFrom);
                    long dateToLong = GeneralFunc.convertDateStringToLong(dayTo+" "+hourTo);
                    Date dateFrom = new Date(dateFromLong);
                    Date dateTo = new Date(dateToLong);
                    if(dateTo.before(dateFrom)){
                        Toast.makeText(getActivity(),"DateTime is wrong, date from is greater than date to",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mFilter.setFromDateTime(dateFromLong);
                    mFilter.setToDateTime(dateToLong);
                }
                mFilter.dumpToLog();
                mInterface.onApplyFilter(mFilter);
                getDialog().dismiss();
            }
        });

        imvDayFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeTimeFilter = AdminUserRequestDialog.TYPE_TIME_FILTER.FROM;
                typeChooseTimeFilter = AdminUserRequestDialog.TYPE_CHOOSE_TIME_FILTER.DAY;
                new AdminUserRequestDialog.DatePickerDialog().show(getActivity().getSupportFragmentManager(),null);
            }
        });

        imvDayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeTimeFilter = AdminUserRequestDialog.TYPE_TIME_FILTER.TO;
                typeChooseTimeFilter = AdminUserRequestDialog.TYPE_CHOOSE_TIME_FILTER.DAY;
                new AdminUserRequestDialog.DatePickerDialog().show(getActivity().getSupportFragmentManager(),null);
            }
        });

        imvHourFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeTimeFilter = AdminUserRequestDialog.TYPE_TIME_FILTER.FROM;
                typeChooseTimeFilter = AdminUserRequestDialog.TYPE_CHOOSE_TIME_FILTER.HOUR;
                new AdminUserRequestDialog.HourPickerDialog().show(getActivity().getSupportFragmentManager(),null);
            }
        });

        imvHourTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeTimeFilter = AdminUserRequestDialog.TYPE_TIME_FILTER.TO;
                typeChooseTimeFilter = AdminUserRequestDialog.TYPE_CHOOSE_TIME_FILTER.HOUR;
                new AdminUserRequestDialog.HourPickerDialog().show(getActivity().getSupportFragmentManager(),null);
            }
        });
    }

    public static class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            switch (typeTimeFilter){
                case FROM:
                    edtDayFrom.setText(day+"/"+(month+1)+"/"+year);
                    break;
                case TO:
                    edtDayTo.setText(day+"/"+(month+1)+"/"+year);
                    break;
            }
        }
    }

    public static class HourPickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            switch (typeTimeFilter){
                case FROM:
                    edtHourFrom.setText(hour+":"+minute+":00");
                    break;
                case TO:
                    edtHourTo.setText(hour+":"+minute+":00");
                    break;
            }
        }
    }
}
