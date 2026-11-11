package cz.elixirstesti;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.Color;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.text.Normalizer;
import java.util.Locale;

public class MainActivity extends Activity {
  int gold=Color.rgb(214,177,105), ivory=Color.rgb(245,228,198), bg=Color.rgb(19,15,20), wine=Color.rgb(91,25,44);
  LinearLayout page; android.content.SharedPreferences prefs;
  String[] places={"Křivoklát","Krakovec","Skryjská jezírka","Hořovice","Křivoklát"};
  String[] answers={"KRAKOVEC","SKRYJSKAJEZIRKA","HOROVICE","KRIVOKLAT","3543"};
  String[] titles={"Královská mřížka","Zrcadlo zrádce","Vodní souřadnice","Tři chodby zámku","Poslední konkláve"};
  String[] stories={
    "Hradní pán přišel o Elixír štěstí. Zrádce odnesl tajemství do šesti sloupců. Písařův klíč je ELIXÍR? Ne! Pro tuto dochovanou šifru platí původní klíč KORUNA. Otevřete papírovou obálku 1 a rozluštěte zprávu.",
    "Na zdech Krakovce čeká zrcadlová zpráva. Někdo ji nejen zakódoval, ale i obrátil. Otevřete papírovou obálku 2.",
    "Vodní strážce u jezírek zaznamenal souřadnice. Jedna dvojice je návnada zrádce. Luštěte papírovou obálku 3 až na bezpečném místě.",
    "Zámecký písař poslal zprávu třemi chodbami. Sledujte cikcak a otevřete papírovou obálku 4.",
    "Rada věrných se vrací na Křivoklát. Sestavte čtyři pečetě z názvů navštívených míst. Otevřete papírovou obálku 5 a zadejte čtyřmístný kód."
  };
  @Override public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences("hra",MODE_PRIVATE);show();}
  String norm(String s){return Normalizer.normalize(s.toUpperCase(new Locale("cs","CZ")),Normalizer.Form.NFD).replaceAll("\\p{M}","").replaceAll("[^A-Z0-9]","");}
  TextView text(String s,int size,int color){TextView t=new TextView(this);t.setText(s);t.setTextColor(color);t.setTextSize(size);t.setPadding(0,9,0,12);page.addView(t);return t;}
  void button(String label,Runnable action){Button b=new Button(this);b.setText(label);b.setTextColor(ivory);b.setAllCaps(false);GradientDrawable d=new GradientDrawable();d.setColor(wine);d.setCornerRadius(22);d.setStroke(2,gold);b.setBackground(d);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,12,0,12);page.addView(b,lp);b.setOnClickListener(v->action.run());}
  void picture(String file,int height){
    try {
      Bitmap bmp=BitmapFactory.decodeStream(getAssets().open(file));
      ImageView img=new ImageView(this);
      img.setImageBitmap(bmp);img.setAdjustViewBounds(true);
      img.setScaleType(ImageView.ScaleType.FIT_CENTER);
      LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(height));
      lp.setMargins(0,8,0,12);page.addView(img,lp);
    } catch(Exception ignored){}
  }
  int dp(int n){return (int)(n*getResources().getDisplayMetrics().density);}
  void show(){
    int stage=prefs.getInt("stage",0), mode=prefs.getInt("mode",0);
    ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(bg);
    try{
      Bitmap bm=BitmapFactory.decodeStream(getAssets().open("background.png"));
      GradientDrawable tint=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0xEE090A11,0xF5100810});
      scroll.setBackgroundColor(bg);
    }catch(Exception ignored){}
    page=new LinearLayout(this);page.setOrientation(LinearLayout.VERTICAL);page.setPadding(28,42,28,38);scroll.addView(page);setContentView(scroll);
    TextView brand=text("✦  RADA VĚRNÝCH  ✦",14,gold);brand.setGravity(Gravity.CENTER);
    TextView name=text("ZTRACENÝ ELIXÍR ŠTĚSTÍ\nHRADNÍHO PÁNA",25,ivory);name.setTypeface(null,Typeface.BOLD);name.setGravity(Gravity.CENTER);
    if(stage==0 && mode==0){picture("envelope.png",190);}
    else if(stage==4 && mode==2){picture("chest.png",200);}
    else if(mode==2){picture("seals.png",145);}
    else {picture(new String[]{"portrait_petr.png","portrait_barca.png","portrait_valentyna.png","portrait_ema.png","portrait_petr.png"}[stage],155);}
    text("KAPITOLA "+(stage+1)+" / 5  •  "+places[stage],13,gold);
    if(stage==0&&mode==0){text("Na hradě Křivoklát zmizel vzácný Elixír štěstí hradního pána. Jeden ze zrádců jej odnesl a zanechal čtyři zašifrované zprávy. Jste Rada věrných. Ne každá stopa je pravdivá. Věřte důkazům, ne náhodě.",19,ivory);button("Zahájit pátrání",()->set(0,1));return;}
    if(mode==2){
      if(stage==4){text("Dokázali jste odhalit kód! Předejte jej strážci skutečné truhly. Elixír štěstí je zachráněn.",20,ivory);button("Hrát znovu",()->new AlertDialog.Builder(this).setMessage("Opravdu začít znovu?").setPositiveButton("Ano",(d,w)->set(0,0)).setNegativeButton("Ne",null).show());return;}
      text("Pečeť uznána. Další stopa čeká na místě:",17,ivory);
      TextView next=text(places[stage+1].toUpperCase(new Locale("cs","CZ")),25,gold);next.setTypeface(null,Typeface.BOLD);
      text(stage==1?"Vydejte se k parkovišti Slap a pak pěšky ke Skryjským jezírkům.":"Na cestu vyrazte až s rodiči. Šifry nikdy neluštěte při řízení.",15,ivory);
      button("Jsme na místě – otevřít další misi",()->set(stage+1,1));return;
    }
    text(titles[stage],22,gold);
    text(stories[stage],17,ivory);
    text("Použijte papírovou obálku č. "+(stage+1)+". Do telefonu zadejte pouze výslednou odpověď.",14,ivory);
    EditText input=new EditText(this);input.setSingleLine(true);input.setTextColor(ivory);input.setHintTextColor(gold);input.setHint(stage==4?"Čtyřmístný kód":"Název dalšího místa");input.setInputType(stage==4?2:1);page.addView(input);
    button("Ověřit odpověď",()->{
      if(norm(input.getText().toString()).equals(answers[stage]))set(stage,2);
      else new AlertDialog.Builder(this).setTitle("Zrádce vás zmátl").setMessage("Tato odpověď nesouhlasí. Zkontrolujte šifru a zkuste to znovu.").setPositiveButton("Pokračovat",null).show();
    });
    text("Bez GPS • bez internetu • bez registrace\nPostup se ukládá v tomto telefonu.",12,gold);
  }
  void set(int stage,int mode){prefs.edit().putInt("stage",stage).putInt("mode",mode).apply();show();}
}
