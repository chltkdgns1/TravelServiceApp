package com.alsdnjsrl.snsproject.listener;

import com.alsdnjsrl.snsproject.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}