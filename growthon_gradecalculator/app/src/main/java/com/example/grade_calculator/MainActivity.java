package com.example.yourappname;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grade_calculator.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button addSubjectButton, calculateButton;
    private int subjectCount = 1;
    private HashMap<Integer, Spinner> gradeSpinners = new HashMap<>();
    private HashMap<Integer, CheckBox> majorCheckBoxes = new HashMap<>();
    private static final String[] grades = {"A+", "A0", "B+", "B0", "C+", "C0", "D+", "D0", "F"};
    private static final double[] gradeValues = {4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        addSubjectButton = findViewById(R.id.add_subject_button);
        calculateButton = findViewById(R.id.calculate_button);

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectRow();
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateGrades();
            }
        });

        // 첫 번째 과목의 스피너와 체크박스를 초기화
        initializeSpinnerAndCheckBox(1);
    }

    private void addSubjectRow() {
        subjectCount++;
        TableRow newRow = new TableRow(this);

        TextView subjectText = new TextView(this);
        subjectText.setText("과목 " + subjectCount);
        subjectText.setPadding(8, 8, 8, 8);

        TextView creditText = new TextView(this);
        creditText.setText("0"); // 학점을 입력받도록 설정
        creditText.setPadding(8, 8, 8, 8);

        Spinner gradeSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);

        CheckBox majorCheckBox = new CheckBox(this);

        newRow.addView(subjectText);
        newRow.addView(creditText);
        newRow.addView(gradeSpinner);
        newRow.addView(majorCheckBox);

        tableLayout.addView(newRow);
        gradeSpinners.put(subjectCount, gradeSpinner);
        majorCheckBoxes.put(subjectCount, majorCheckBox);
    }

    private void initializeSpinnerAndCheckBox(int index) {
        Spinner gradeSpinner = findViewById(getResources().getIdentifier("grade_spinner" + index, "id", getPackageName()));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);

        CheckBox majorCheckBox = findViewById(getResources().getIdentifier("major_checkbox" + index, "id", getPackageName()));
        gradeSpinners.put(index, gradeSpinner);
        majorCheckBoxes.put(index, majorCheckBox);
    }

    private void calculateGrades() {
        double totalPoints = 0;
        int totalCredits = 0;

        for (int i = 1; i <= subjectCount; i++) {
            Spinner gradeSpinner = gradeSpinners.get(i);
            CheckBox majorCheckBox = majorCheckBoxes.get(i);

            if (gradeSpinner != null && majorCheckBox != null) {
                String grade = gradeSpinner.getSelectedItem().toString();
                double gradeValue = getGradeValue(grade);

                int credits = Integer.parseInt(((TextView)((TableRow) gradeSpinner.getParent()).getChildAt(1)).getText().toString());

                totalPoints += gradeValue * credits;
                totalCredits += credits;
            }
        }

        if (totalCredits > 0) {
            double gpa = totalPoints / totalCredits;
            Toast.makeText(this, "평균 평점: " + gpa, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "학점을 입력하세요.", Toast.LENGTH_LONG).show();
        }
    }

    private double getGradeValue(String grade) {
        for (int i = 0; i < grades.length; i++) {
            if (grades[i].equals(grade)) {
                return gradeValues[i];
            }
        }
        return 0;
    }
}
