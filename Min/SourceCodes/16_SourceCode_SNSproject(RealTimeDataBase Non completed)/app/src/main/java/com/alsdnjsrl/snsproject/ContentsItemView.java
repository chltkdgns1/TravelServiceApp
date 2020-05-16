/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alsdnjsrl.snsproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.alsdnjsrl.snsproject.R;
import com.bumptech.glide.Glide;

public class ContentsItemView extends LinearLayout {
    private ImageView imageView;
    private EditText editText;

    public ContentsItemView(Context context) {
        super(context);
        initView();
    }

    public ContentsItemView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(layoutInflater.inflate(R.layout.view_contents_image, this, false));
        addView(layoutInflater.inflate(R.layout.view_contents_edit_text, this, false));

        imageView = findViewById(R.id.contentsImageView);
        editText = findViewById(R.id.contentsEditText);
    }

    public void setImage(String path){
        Glide.with(this).load(path).override(1000).into(imageView);
    }

    public void setText(String text){
        editText.setText(text);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        imageView.setOnClickListener(onClickListener);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener){
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }
}