package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

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
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Ultils.GeneralFunc;

import java.util.Calendar;
import java.util.Date;

import static com.example.staffmanagement.View.Data.StaffRequestFilter.SORT.None;

public class StaffRequestFilterDialog extends DialogFragment {

    public enum TYPE_TIME_FILTER {
        FROM, TO
    }

    public enum TYPE_CHOOSE_TIME_FILTER {
        DAY, HOUR
    }

    private CheckBox cbWaiting, cbAccept, cbDecline;
    private RadioButton rbSortNone, rbSortTitle, rbSortDateTime, rbSortTitleAsc, rbSortDateTimeAsc, rbSortTitleDesc, rbSortDateTimeDesc;
    private RadioGroup rgParent;
    private static EditText edtDayFrom;
    private static EditText edtDayTo;
    private static EditText edtHourFrom;
    private static EditText edtHourTo;
    private ImageView imvDayFrom, imvDayTo, imvHourFrom, imvHourTo;
    private TextView txtClose, txtAccept, txtReset;
    protected static TYPE_TIME_FILTER typeTimeFilter;
    protected static TYPE_CHOOSE_TIME_FILTER typeChooseTimeFilter;
    private StaffRequestFilter mCriteria;
    private StaffRequestInterface mInterface;
    public StaffRequestFilterDialog(StaffRequestFilter mCriteria,StaffRequestInterface mInterface) {
        this.mInterface = mInterface;
        this.mCriteria = mCriteria;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_staff_request_filter, container, false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mInterface = null;
        edtDayFrom = null;
        edtDayTo = null;
        edtHourFrom = null;
        edtHourTo = null;
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

    private void prepareData() {
        prepareDataForCheckBox();
        prepareDataForRadioButton();
        prepareDataForEditTextDateTime();
    }

    private void prepareDataForCheckBox() {
        for (StaffRequestFilter.STATE s : mCriteria.getStateList()) {
            if (s.equals(StaffRequestFilter.STATE.Waiting))
                cbWaiting.setChecked(true);
            else if (s.equals(StaffRequestFilter.STATE.Accept))
                cbAccept.setChecked(true);
            else if (s.equals(StaffRequestFilter.STATE.Decline))
                cbDecline.setChecked(true);
        }
    }

    private void prepareDataForRadioButton() {
        switch (mCriteria.getSortName()) {
            case None:
                break;
            case Title:
                setEnableRbTitle(true);
                rbSortTitle.setChecked(true);
                if (mCriteria.getSortType().equals(StaffRequestFilter.SORT_TYPE.ASC))
                    rbSortTitleAsc.setChecked(true);
                else
                    rbSortTitleDesc.setChecked(true);
                break;
            case DateTime:
                setEnableRbDateTime(true);
                rbSortDateTime.setChecked(true);
                if (mCriteria.getSortType().equals(StaffRequestFilter.SORT_TYPE.ASC))
                    rbSortDateTimeAsc.setChecked(true);
                else
                    rbSortDateTimeDesc.setChecked(true);
                break;
        }
    }

    private void prepareDataForEditTextDateTime() {
        if (mCriteria.getFromDateTime() != 0 && mCriteria.getToDateTime() != 0) {
            String[] dayFromArr = GeneralFunc.convertMilliSecToDateString(mCriteria.getFromDateTime()).split(" ");
            String[] dayToArr = GeneralFunc.convertMilliSecToDateString(mCriteria.getToDateTime()).split(" ");
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
                mInterface.onCancelFilter();
            }
        });

        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickResetDataFilter();
            }
        });

        rgParent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                onChangeRadioButtonState(radioGroup);
            }
        });

        txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GeneralFunc.checkInternetConnection(getActivity()))
                    onClickAcceptFilter();
            }
        });

        imvDayFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickImageDayFrom();
            }
        });

        imvDayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickImageDayTo();
            }
        });

        imvHourFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickImageHourFrom();
            }
        });

        imvHourTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickImageHourTo();
            }
        });
    }

    private void onClickImageDayFrom() {
        typeTimeFilter = TYPE_TIME_FILTER.FROM;
        typeChooseTimeFilter = TYPE_CHOOSE_TIME_FILTER.DAY;
        new DatePickerDialog().show(getActivity().getSupportFragmentManager(), null);
    }

    private void onClickImageDayTo() {
        typeTimeFilter = TYPE_TIME_FILTER.TO;
        typeChooseTimeFilter = TYPE_CHOOSE_TIME_FILTER.DAY;
        new DatePickerDialog().show(getActivity().getSupportFragmentManager(), null);
    }

    private void onClickImageHourFrom() {
        typeTimeFilter = TYPE_TIME_FILTER.FROM;
        typeChooseTimeFilter = TYPE_CHOOSE_TIME_FILTER.HOUR;
        new HourPickerDialog().show(getActivity().getSupportFragmentManager(), null);
    }

    private void onClickImageHourTo() {
        typeTimeFilter = TYPE_TIME_FILTER.TO;
        typeChooseTimeFilter = TYPE_CHOOSE_TIME_FILTER.HOUR;
        new HourPickerDialog().show(getActivity().getSupportFragmentManager(), null);
    }

    private void onClickAcceptFilter() {
        handleAcceptFilterCheckBox();
        handleAcceptFilterRadioButton();
        handleAcceptFilterEditTextTime();
    }

    private void onClickResetDataFilter() {
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

    private void handleAcceptFilterCheckBox() {

        if (cbWaiting.isChecked() && !mCriteria.getStateList().contains(StaffRequestFilter.STATE.Waiting))
            mCriteria.getStateList().add(StaffRequestFilter.STATE.Waiting);
        else if (!cbWaiting.isChecked() && mCriteria.getStateList().contains(StaffRequestFilter.STATE.Waiting))
            mCriteria.getStateList().remove(StaffRequestFilter.STATE.Waiting);

        if (cbAccept.isChecked() && !mCriteria.getStateList().contains(StaffRequestFilter.STATE.Accept))
            mCriteria.getStateList().add(StaffRequestFilter.STATE.Accept);
        else if (!cbAccept.isChecked() && mCriteria.getStateList().contains(StaffRequestFilter.STATE.Accept))
            mCriteria.getStateList().remove(StaffRequestFilter.STATE.Accept);

        if (cbDecline.isChecked() && !mCriteria.getStateList().contains(StaffRequestFilter.STATE.Decline))
            mCriteria.getStateList().add(StaffRequestFilter.STATE.Decline);
        else if (!cbDecline.isChecked() && mCriteria.getStateList().contains(StaffRequestFilter.STATE.Decline))
            mCriteria.getStateList().remove(StaffRequestFilter.STATE.Decline);
    }

    private void handleAcceptFilterRadioButton() {
        int idParentRaGr = rgParent.getCheckedRadioButtonId();
        switch (idParentRaGr) {
            case R.id.radioButton_sort_none_filter:
                mCriteria.setSortName(None);
                mCriteria.setSortType(StaffRequestFilter.SORT_TYPE.None);
                break;
            case R.id.radioButton_sort_title_filter:
                mCriteria.setSortName(StaffRequestFilter.SORT.Title);
                if (rbSortTitleAsc.isChecked())
                    mCriteria.setSortType(StaffRequestFilter.SORT_TYPE.ASC);
                else
                    mCriteria.setSortType(StaffRequestFilter.SORT_TYPE.DESC);
                break;
            case R.id.radioButton_sort_dateTime_filter:
                mCriteria.setSortName(StaffRequestFilter.SORT.DateTime);
                if (rbSortDateTimeAsc.isChecked())
                    mCriteria.setSortType(StaffRequestFilter.SORT_TYPE.ASC);
                else
                    mCriteria.setSortType(StaffRequestFilter.SORT_TYPE.DESC);
                break;
        }
    }

    private void handleAcceptFilterEditTextTime() {
        String dayFrom = edtDayFrom.getText().toString();
        String dayTo = edtDayTo.getText().toString();
        String hourFrom = edtHourFrom.getText().toString();
        String hourTo = edtHourTo.getText().toString();

        // check if exist one of the time field has data, then must be input all time field
        if (!TextUtils.isEmpty(dayFrom) || !TextUtils.isEmpty(dayTo) || !TextUtils.isEmpty(hourFrom) || !TextUtils.isEmpty(hourTo)) {

            if (TextUtils.isEmpty(dayFrom)) {
                showMessageForDateTimePicker("Day from is empty");
                return;
            } else if (TextUtils.isEmpty(dayTo)) {
                showMessageForDateTimePicker("Day to is empty");
                return;
            } else if (TextUtils.isEmpty(hourFrom)) {
                showMessageForDateTimePicker("Hour from is empty");
                return;
            } else if (TextUtils.isEmpty(hourTo)) {
                showMessageForDateTimePicker("Hour to is empty");
                return;
            }

        }

        // case 1 : check if all time field not empty then get time from field
        if (!TextUtils.isEmpty(dayFrom) && !TextUtils.isEmpty(dayTo) && !TextUtils.isEmpty(hourFrom) && !TextUtils.isEmpty(hourTo)) {
            long dateFromLong = GeneralFunc.convertDateStringToLong(dayFrom + " " + hourFrom);
            long dateToLong = GeneralFunc.convertDateStringToLong(dayTo + " " + hourTo);
            Date dateFrom = new Date(dateFromLong);
            Date dateTo = new Date(dateToLong);
            if (dateTo.before(dateFrom)) {
                Toast.makeText(getActivity(), "DateTime is wrong, date from is greater than date to", Toast.LENGTH_SHORT).show();
                return;
            }
            mCriteria.setFromDateTime(dateFromLong);
            mCriteria.setToDateTime(dateToLong);
        }

        // case 2 : if empty all field, it means reset the filter
        if(TextUtils.isEmpty(dayFrom) && TextUtils.isEmpty(dayTo) && TextUtils.isEmpty(hourFrom) && TextUtils.isEmpty(hourTo)) {
            mCriteria.setFromDateTime(0);
            mCriteria.setToDateTime(0);
        }

        mCriteria.dumpToLog();
        getDialog().dismiss();
        mInterface.onApplyFilter(mCriteria);
    }

    private void onChangeRadioButtonState(RadioGroup radioGroup) {
        int idGroup = radioGroup.getCheckedRadioButtonId();
        switch (idGroup) {
            case R.id.radioButton_sort_none_filter:
                setCheckedRadioButton(false);
                setEnableRadioButton(false);
                break;
            case R.id.radioButton_sort_title_filter:
                setEnableRbTitle(true);
                rbSortTitleAsc.setChecked(true);
                if (!rbSortTitleAsc.isChecked())
                    rbSortTitleDesc.setChecked(true);
                setCheckedRbDateTime(false);
                setEnableRbDateTime(false);
                break;
            case R.id.radioButton_sort_dateTime_filter:
                setEnableRbDateTime(true);
                rbSortDateTimeAsc.setChecked(true);
                if (!rbSortDateTimeAsc.isChecked())
                    rbSortDateTimeDesc.setChecked(true);
                setCheckedRbTitle(false);
                setEnableRbTitle(false);
                break;
        }
    }

    private void showMessageForDateTimePicker(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Your must choose entire day and time or not choose any day time", Toast.LENGTH_SHORT).show();
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
            switch (typeTimeFilter) {
                case FROM:
                    edtDayFrom.setText(day + "/" + (month + 1) + "/" + year);
                    break;
                case TO:
                    edtDayTo.setText(day + "/" + (month + 1) + "/" + year);
                    break;
            }
        }
    }

    public static class HourPickerDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            switch (typeTimeFilter) {
                case FROM:
                    edtHourFrom.setText(hour + ":" + minute + ":00");
                    break;
                case TO:
                    edtHourTo.setText(hour + ":" + minute + ":00");
                    break;
            }
        }
    }
}
