package com.example.android.ctc;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;


import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.lang.Math;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.ctc.databinding.ActivityMainBinding;
import com.example.android.ctc.databinding.FragmentSecondBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfDocument;
//import com.itextpdf.text.pdf.PdfWriter;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private EditText inputEditText;
    public String three_ms_voice_activated;
    TextToSpeech tts;
    public String calculated_as;
    public String calculation_type;
    public String input_values = "";
    private static final int REQUEST_PERMISSION = 1;

    //permission
//    Boolean boolean_permission;
//
    ScrollView II_pdflayout; //save the layout of the pdf
//    Bitmap bitmap;


//    public Document document = new Document();

    public Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


//        II_pdflayout = (ScrollView) findViewById(R.id.pdf);
//
//        bitmap = loadBitmapFromView(II_pdflayout, II_pdflayout.getWidth(), II_pdflayout.getHeight());


        String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE};
        int requestExternalStorage = 1;
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsStorage, requestExternalStorage);
        }


        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "Thia language is not supported");
                    } else {
                        ConvertTextToSpeech();
                    }
                } else {
                    Log.e("error", "Initilization failed");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public void Calculate(View v) {
        //get values from input values text input
        inputEditText = findViewById(R.id.inputValues);
        String input = inputEditText.getText().toString();
        input_values = input;

        //split the values using , into input elements array
        String[] inputElements = input.split(",");

        //make a new array of the same size but of the type double
        int n = inputElements.length;
        int myValues[] = new int[n];

        //fore each element in the initial array, convert the string elements to type double
        for (int i = 0; i < inputElements.length; i++) {
            int value = Integer.parseInt(inputElements[i]);
            myValues[i] = value;
        }


        //Calculate mean
        int sum = 0;

        for (int i = 0; i < myValues.length; i++) {
            sum += myValues[i];
        }
        int mean;
        mean = sum / myValues.length;


        //Calculate median
        // first sort the array in ascending order

        Arrays.sort(myValues);
        int middle = myValues.length / 2;
        int median = 0;

        if (myValues.length % 2 == 1) {
            median = myValues[middle];
        } else {
            median = (myValues[middle - 1] + myValues[middle]) / 2;
        }

        //Calculate Mode
        final List<Integer> modes = new ArrayList<Integer>();
        final Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();

        int max = -1;

        for (final int k : myValues) {
            int count = 0;

            if (countMap.containsKey(k)) {
                count = countMap.get(k) + 1;
            } else {
                count = 1;
            }

            countMap.put(k, count);

            if (count > max) {
                max = count;
            }
        }

        for (final Map.Entry<Integer, Integer> tuple : countMap.entrySet()) {
            if (tuple.getValue() == max) {
                modes.add(tuple.getKey());
            }
        }

        String mode_tts;
        if (modes.size() > 1) {
            mode_tts = "the modes are";
        } else {
            mode_tts = "";
        }

        for (int j = 0; j < modes.size(); j++) {
            if (j == 0) {
                mode_tts += " " + String.valueOf(modes.get(j));
            } else if (j == modes.size() - 1) {

                mode_tts += "and " + String.valueOf(modes.get(j));
            } else {
                mode_tts += ", " + String.valueOf(modes.get(j));
            }

        }
//        String mode_tts = "3";
//        String mean = "1";
//        String median = "4";
        three_ms_voice_activated = "The mean is " + String.valueOf(mean) + "the median is " + String.valueOf(median) + mode_tts;

//        TextView mean_out = (TextView) findViewById(R.id.Mean_out);
//        TextView median_out = (TextView) findViewById(R.id.Median_out);
//        TextView mode_out = (TextView) findViewById(R.id.Mode_out);

//        mean_out.setText("Mean: " + String.valueOf(mean));
//        median_out.setText("Median: " + String.valueOf(median));
//        mode_out.setText("Mode: " + mode_tts);

        //variance
        double variance = 0;
        for (int i = 0; i < myValues.length; i++) {
            variance += Math.pow(myValues[i] - mean, 2);
        }
        variance /= myValues.length;

        // Standard Deviation
        double std = Math.sqrt(variance);

        String inputvals = "";

        if (v.getId() == R.id.btn_mean) {
            three_ms_voice_activated = "The mean is:    " + String.valueOf(mean);
            calculation_type = "MEAN";
            calculated_as = "The formula for calculating the mean, also known as the arithmetic mean, of a set of numbers is:\n" +
                    "\n" +
                    "mean = (sum of all numbers) / (total number of numbers)\n" +
                    "\n" +
                    "In mathematical notation, this can be represented as:\n" +
                    "\n" +
                    "mean = (x1 + x2 + x3 + ... + xn) / n\n" +
                    "\n" +
                    "Where x1, x2, x3, ..., xn are the individual numbers in the set, and n is the total number of numbers in the set.\n" +
                    "\n" +
                    "For example, given your set of numbers:" +

                    "{" +
                    input +
                    "}, " +

                    "the mean would be calculated as:\n" +
                    "\n" +
                    "mean = (";


            for (int i = 0; i < inputElements.length; i++) {
                if (i != inputElements.length - 1) {
                    inputvals += inputElements[i] + " + ";
                } else {
                    inputvals += inputElements[i];
                }

            }

            calculated_as += inputvals + ") / " +
                    Integer.toString(inputElements.length) +
                    " =" +
                    Integer.toString(sum) +
                    "/ " +
                    Integer.toString(inputElements.length) +
                    "= " + Integer.toString(mean) +
                    "\n" +
                    "\n" +
                    "So the mean of the set is " +
                    Integer.toString(mean) +
                    ".";


            ConvertTextToSpeech();
        } else if (v.getId() == R.id.btn_mode) {
            three_ms_voice_activated = "the mode is:    " + mode_tts;
            calculation_type = "MODE";
            calculated_as = "The mode is the value that appears most frequently in a data set. To calculate the mode, you first need to list all of the values in the data set and count how many times each value appears. Then, you identify the value(s) that appear the most and these are the mode(s) of the data set.\n" +
                    "\n" +
                    "Formula for calculating mode:\n" +
                    "\n" +
                    "mode = the value(s) that appear most frequently in the data set." +
                    "The mode is :" + mode_tts;

            ConvertTextToSpeech();
        } else if (v.getId() == R.id.btn_median) {
            three_ms_voice_activated = "the median is:  " + String.valueOf(median);
            calculation_type = "MEDIAN";
            calculated_as = "The median is the middle value in a data set when the values are arranged in order. To calculate the median, you first need to arrange all of the values in the data set in numerical order.\n" +
                    "\n" +
                    "If the data set contains an odd number of values, the median is the value that is in the middle of the ordered list.\n" +
                    "\n" +
                    "If the data set contains an even number of values, the median is the average of the two middle values.\n" +
                    "\n" +
                    "Formula for calculating median:\n" +
                    "\n" +
                    "median =\n" +
                    "\n" +
                    "the middle value (if the data set contains an odd number of values)\n" +
                    "the average of the two middle values (if the data set contains an even number of values)\n" +
                    "To find the median you can use the order statistics algorithm and more specifically the quick select algorithm. It can find the median in linear time O(n) as average case and O(n^2) as worst case." +
                    "\n" +
                    "the median is:  " + String.valueOf(median);


            ConvertTextToSpeech();
        } else if (v.getId() == R.id.btn_variance) {
            three_ms_voice_activated = "the variance is:    " + String.valueOf(variance);
            calculation_type = "VARIANCE";
            calculated_as = "The variance is a measure of the spread of a data set. It tells you how far each data point is from the mean of the data set.\n" +
                    "\n" +
                    "To calculate the variance, you first need to find the mean of the data set. Then, for each data point, you calculate the difference between the data point and the mean, square the difference, and add all of the squared differences together. Finally, you divide the sum of the squared differences by the number of data points in the data set (or by the number of data points minus one, if you are calculating the sample variance).\n" +
                    "\n" +
                    "Formula for calculating variance given your data set is as:\n" +
                    "\n";

            inputvals = "";
            for (int i = 0; i < inputElements.length; i++) {
                if (i != inputElements.length - 1) {
                    inputvals += inputElements[i] + " + ";
                } else {
                    inputvals += inputElements[i];
                }

            }

            calculated_as += "mean = " + inputvals + ") / " +
                    Integer.toString(inputElements.length) +
                    " =" +
                    Integer.toString(sum) +
                    "/ " +
                    Integer.toString(inputElements.length) +
                    "= " + Integer.toString(mean) +
                    "\n" +
                    "\n" +
                    "So the mean of the set is " +
                    Integer.toString(mean) +
                    ".\n\n";

            calculated_as += "then we calculate each value's difference from the Mean\n\n";


            calculated_as += "To calculate the Variance, take each difference, square it, and then average the result:\n" +
                    "\n" +
                    "Variance\n\n" +
                    "σ2\t=\t";

            String inputvals2 = "";
            for (int i = 0; i < inputElements.length; i++) {
                if (i != inputElements.length - 1) {
                    inputvals2 += " " + Integer.toString(Integer.parseInt(inputElements[i]) - mean) + " +";
                } else {
                    inputvals2 += " " + Integer.toString(Integer.parseInt(inputElements[i]) - mean);
                }

            }
            calculated_as += "(" + inputvals2 + ") Each Value Squared" +
                    "/" + Integer.toString(inputElements.length);


            ConvertTextToSpeech();

        } else if (v.getId() == R.id.btn_standard_deviation) {
            three_ms_voice_activated = "the deviation is:   " + String.valueOf(std);
            calculation_type = "STANDARD DEVIATION";
            calculated_as = "The standard deviation is a measure of the spread of a data set, similar to the variance. It tells you how far each data point is from the mean of the data set, but it is expressed in the same units as the data points, rather than in squared units.\n" +
                    "\n" +
                    "To calculate the standard deviation, you first calculate the variance of the data set. Then, you take the square root of the variance.\n" +
                    "\n" +
                    "Formula for calculating standard deviation:\n" +
                    "\n" + "σ\t=\t√" + String.valueOf(variance);


            ConvertTextToSpeech();
        }


        tosecond();
        savepdf();
        isStoragePermissionGranted();


    }

    public void savepdf_pdf() {
        String W = "win";
//        savepdf();
    }

    public void tosecond() {
        CTC frag = CTC.GetInstance();
        frag.toSecond();


    }

    public void tofirst() {
        CtcResults frag = CtcResults.GetInstance();
        frag.toFirst();

    }

    public void ConvertTextToSpeech() {
        if (three_ms_voice_activated == null || "".equals(three_ms_voice_activated)) {
            three_ms_voice_activated = "input values to calculate mean, median and mode";
            tts.speak(three_ms_voice_activated, TextToSpeech.QUEUE_FLUSH, null);
        } else {

            tts.speak(three_ms_voice_activated + "," + "\n" + calculated_as, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Message", "Permission is granted");
                return true;
            } else {

                Log.v("Message", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Message", "Permission is granted");
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Accepted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void btnOnClick(View v) {
        Toast.makeText(this, "calculation saved as ctc_calculations.pdf in download folder", Toast.LENGTH_LONG).show();
    }


    public void savepdf() {
//        try {
//            String result = three_ms_voice_activated;
//
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault());
//            String dateTime = dateFormat.format(calendar.getTime());
//
//
//
//            String pdfName = "CTC_calculations.pdf";
//
//            inputEditText = findViewById(R.id.inputValues);
////            String input = inputEditText.getText().toString();
//
//
//            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
////            File path = Environment.getExternalStorageDirectory();
////            File path2 = Context.getExternalFilesDir(null)
//            File pdfFile = new File(path, "ctc_calculations.pdf");
//            Toast.makeText(this, "Saved  to downloads/ctc_calculation.pdf", Toast.LENGTH_SHORT).show();
//
//
//
//            if (!pdfFile.exists()) {
//                try {
//                    pdfFile.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
//            document.open();
//            document.add(new Paragraph("||||||||||| :" + dateTime));
//            document.add(new Paragraph("Given :" +  ""+three_ms_voice_activated));
//            document.add(new Paragraph("        :" + three_ms_voice_activated));
//            document.add(new Paragraph("||||||||||| :" + dateTime));
//            document.close();
////            Toast.makeText(this, "calculation saved as ctc_calculations.pdf in download folder", Toast.LENGTH_LONG).show();
//        } catch (DocumentException | FileNotFoundException e) {
//            e.printStackTrace();
////            Toast.makeText(this, "requires android sdk < 29", Toast.LENGTH_LONG).show();
//        }

        //save pdf version 2
//        try {
//
//              String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//            PdfDocument myPdf = new PdfDocument();
//            Paint myPaint = new Paint();
//
//            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
//            PdfDocument.Page myPage1 = myPdf.startPage(myPageInfo1);
//
//            Canvas canvas1 = myPage1.getCanvas();
//
//            // Write the pdf
//
//            String text = "\n\n\n" + ""; //todo
//
//            String[] lines = text.split("\n");
//            float textsize = -myPaint.ascent() + myPaint.descent();
//            if (myPaint.getStyle() == Paint.Style.FILL_AND_STROKE || myPaint.getStyle() == Paint.Style.STROKE) {
//                textsize += myPaint.getStrokeWidth();
//            }
//            float linespace = textsize * 0.2f;
//            float x = 0;
//            float y = 0;
//            for (int i = 0; i < lines.length; i++) {
//                canvas1.drawText(lines[i], x, y + (textsize + linespace) * i, myPaint);
//            }
//
//            myPdf.finishPage(myPage1);
//            String filename = "CTC SOLVER" + System.currentTimeMillis() + ".pdf";
//            File file = new File(pdfpath, filename);
//            myPdf.writeTo(new FileOutputStream(file));
//
//            Toast.makeText(MainActivity.this, "Pdf Saved Successfully", Toast.LENGTH_SHORT).show();
//
//            myPdf.close();
//        }catch (IOException e){
//            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//

        try {
            //save the file in any format not pdf only even image
            ScrollView content = findViewById(R.id.pdf);
            content.setDrawingCacheEnabled(true);
            Bitmap bitmap = content.getDrawingCache();
            File file, f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(android.os.Environment.getExternalStorageDirectory(), "CTC_Image");
                if (!file.exists()) {
                    file.mkdirs();
                }
                f = new File(file.getAbsoluteFile() + file.separator + "filename" + ".png");
            }
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);
            outputStream.close();
            Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
