package com.app.http_animals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.app.http_animals.databinding.ActivityGenerateBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Random;

public class GenerateActivity extends AppCompatActivity {

    ActivityGenerateBinding binding;
    String petName;

    int responseStatus;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.shareBtn.setOnClickListener(view1 -> {
            shareImg(bitmap);
        });

        binding.genBtn.setOnClickListener(view12 -> generateImg());

        Intent intent = getIntent();
        petName = intent.getStringExtra("name");

        generateImg();
    }

    private void shareImg(Bitmap bitmap) {
        Uri uri = getImageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "RESPONSE CODE: " + responseStatus);
        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "My lucky response code :)");
        // setting type to image
        intent.setType("image/png");
        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    // Retrieving the url to share
    private Uri getImageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.anni.shareimage.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    private void generateImg() {
        binding.randNumbLy.setVisibility(View.VISIBLE);
        binding.buttonsLy.setVisibility(View.GONE);
        responseStatus = new Random().nextInt((599 - 100) + 1) + 100;
        binding.randNumbTw.setText(String.format(Locale.getDefault(),"%d", responseStatus));
        String IMAGE_URL = "https://http" + "." + petName + "/" + responseStatus + ".jpg";
        Glide.with(this).load(IMAGE_URL).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                new Handler().post(() -> generateImg());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                binding.randNumbLy.setVisibility(View.GONE);
                binding.buttonsLy.setVisibility(View.VISIBLE);
                //set image bitmap
                bitmap = drawableToBitmap(resource);
                binding.petImg.setImageBitmap(bitmap);
                return false;
            }
        }).transition(DrawableTransitionOptions.withCrossFade()).into(binding.petImg);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}