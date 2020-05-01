package com.alsdnjsrl.travelcourceapp;

public class DrawerLayoutClass {
    // 소스코드 넣을 예정 아래 주석은 참고용으로 아주 가치가 높음

    /*  초기 DrawerLayoutClass 초기화 // 처음 시작하는 거라면 창고로 나쁘지 않습니다.
    private String[] items = {"WHITE", "RED", "GREEN", "BLUE", "BLACK"};
    private ListView listview = null;
    private Activity main_act;

    public DrawerLayoutClass(){}

    void init(Activity act){
        main_act = act;
        ArrayAdapter adapter = new ArrayAdapter(main_act, android.R.layout.simple_list_item_1, items);
        listview = (ListView) act.findViewById(R.id.ListView_Drawer) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                switch (position) { // 클릭했을 때 포지션에 따라서
                    case 0 : // WHITE
                        System.out.println("1");
                        break ;
                    case 1 : // RED
                        System.out.println("2");
                        break ;
                    case 2 : // GREEN
                        System.out.println("3");
                        break ;
                    case 3 : // BLUE
                        System.out.println("4");
                        break ;
                    case 4 : // BLACK
                        System.out.println("5");
                        break ;
                }
                DrawerLayout drawer = (DrawerLayout) main_act.findViewById(R.id.drawer) ;
                drawer.closeDrawer(Gravity.LEFT) ;
            }
        });
    }
     */


}
