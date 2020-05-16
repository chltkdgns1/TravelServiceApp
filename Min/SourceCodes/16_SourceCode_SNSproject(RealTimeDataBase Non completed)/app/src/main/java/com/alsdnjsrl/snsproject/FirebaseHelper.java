package com.alsdnjsrl.snsproject;


import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.alsdnjsrl.snsproject.Util.isStorageUrl;
import static com.alsdnjsrl.snsproject.Util.showToast;
import static com.alsdnjsrl.snsproject.Util.storageUrlToName;

public class FirebaseHelper {
    private Activity activity;
    private OnPostListener onPostListener;
    private int successCount;
    private DatabaseReference myRef;

    public FirebaseHelper(Activity activity) {
        this.activity = activity;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }

    public void storageDelete(final PostInfo postInfo){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final String id = postInfo.getId();
        ArrayList<String> contentsList = postInfo.getContents();
        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrl(contents)) {
                successCount++;

                /*
                    좋은 습관
                    1. 첫째 막히면 찾는다.
                     -> 그거를 그냥 붙여넣기한다. 그리고 아무생각이없다. (나쁜 습관) (대신에 니가 이해를 하려고 노력해)
                     (맨날 해결 못해줘 니가 해야되는데 -> 해결안함 -> 팀원 피해봐)


                    나쁜 습관
                    2. 너는 어떤 기능이 나오지 찾을생각부터해 ( 리얼 개 쓰레기 단점이야)
                        니가 만들생각은 안해? (적어도 니가 만들다가 너무 프로젝트가 커질거같은 경우에 찾아봐)

                    3. 너는 소스코드를 부분적으로 찾으려고하지 않고 완성된 소스코드를 찾으려고해  (더 쓰레기같은 습관)
                     (완성소스코드를 찾잖아 더 이상 이걸로 뭘 할 생각을 안해)

                    4. 니가 찾은 소스코드를 니가 짯다고 생각한다. (마음속으로 니가 합리화를 했겠지, 내가 짠건 아니지만 만족감이 들어)
                    (결과물이 그래 발전이 없어, 니가 짯다고 생각하니까 이해를 간단하고 정작 이해를 못햇어)

                    5. 공부를 할 때 니가 자신감이 넘치는지 과신한건지 아니면 공부 방법을 모르는 건지 그건 잘 모르겠는데,
                     큰 프로젝트에서 에러가 발생했음. 근데 그게 내가 바꾼 소스코드가 문제인 건지 원래 소스코드가 문제인 건지
                     아니면 바꾼 소스코드와 원래 소스코드가 합쳐지면서 생기는 문제인지 모르겠음.

                     새로운 프로젝트를 열어서 firebase 만

                    6 코드를 아까워해? 왜? 이유를 쓸 줄도 모르면서
                    기억이 안나니까 도움이되니까
                    3일 정도 지났지. 니가 그 소스코드를 1줄이라도 이해하려고 노력했음?
                    이미 완성되있으니까

                    기본 : 니가 가져왔으면 무조건 이해를 해라. 니가 짜려면 그것보다 100배는 더 고생한다.

                    정보력 , 실력 x // 개발을 잘한다고 코딩을 잘하는게 아니야

                    새로운 프로젝트를 만들어서 작게 작게 하려고 생각했음

                 */





                StorageReference desertRef = storageRef.child("posts/" + id + "/" + storageUrlToName(contents));
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete(id, postInfo);
                    }
                    // 0  영호형이 영호형은 직접 다 짯잖아
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast(activity, "Error");
                    }
                });
            }
        }
        storeDelete(id, postInfo);
    }

    private void storeDelete(final String id, final PostInfo postInfo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        if (successCount == 0) {
            myRef.child("posts").setValue(id);
        }
    }
}