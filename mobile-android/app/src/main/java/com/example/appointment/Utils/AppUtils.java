package com.example.appointment.Utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appointment.Api.UserApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppUtils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String formatFullName(String name) {
        String[] parts = name.toLowerCase().replaceAll("\\s+", " ").trim().split(" ");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    public static boolean isValidFullName(String name) {
        return name != null && name.matches("^[\\p{L}\\s]+$");
    }
    public static boolean isValidPhone(String phone) {
        return phone.matches("^\\d{9,11}$");
    }
    public static boolean isValidAge(String ageStr) {
        try {
            int age = Integer.parseInt(ageStr);
            return age >= 1 && age <= 120;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidFutureDate(String dateStr) {
        try {
            String[] parts = dateStr.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int year = Integer.parseInt(parts[2]);

            Calendar today = Calendar.getInstance();
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day);
            return !selected.before(today);
        } catch (Exception e) {
            return false;
        }
    }
    public static void setupGenderSpinner(Context context, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, new String[]{"Nam", "Nữ"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static void showDatePicker(Context context, EditText dateField) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(context,
                (view, year, month, day) -> {
                    month++;
                    String formatted = String.format(Locale.US, "%02d/%02d/%04d", day, month, year);
                    dateField.setText(formatted);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    public static String formatDateForAPI(String dateStr) {
        if (dateStr == null || !dateStr.contains("/")) {
            return dateStr;
        }

        String[] parts = dateStr.split("/");
        if (parts.length != 3) {
            return dateStr;
        }

        return parts[0] + "/" + parts[1] + "/" + parts[2];
    }

    public static void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (value.equalsIgnoreCase(adapter.getItem(i).toString())) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    public static void loadDoctors(Context context, Spinner spinnerDoctor, Map<String, Integer> doctorMap) {
        UserApi.getDoctors(context,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("data");
                        List<String> doctorNames = new ArrayList<>();
                        doctorMap.clear(); // Xóa dữ liệu cũ nếu có

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            int id = obj.getInt("doctor_id");
                            String name = obj.getString("fullName") + " (" + obj.getString("specialist") + ")";
                            doctorMap.put(name, id);
                            doctorNames.add(name);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                context,
                                android.R.layout.simple_spinner_item,
                                doctorNames
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDoctor.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Lỗi xử lý dữ liệu bác sĩ", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Lỗi tải danh sách bác sĩ", Toast.LENGTH_SHORT).show()
        );
    }
}
