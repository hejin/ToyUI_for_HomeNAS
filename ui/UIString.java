/*    */ package ui;
/*    */ 
/*    */ import action.FileStationHandler;
/*    */ import java.applet.Applet;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.HashMap;
import java.util.Locale;
/*    */ 
/*    */ public class UIString
/*    */ {
/* 12 */   static String _country = Locale.getDefault().getCountry();
/* 13 */   static String _locale = Locale.getDefault().getLanguage();
/*    */   String localFileName;
/*    */   HashMap<String, String> _StringMap;
/*    */   static String _OKText;
/*    */   static String _UploadText;
/*    */ 
/*    */   UIString(Applet paramApplet)
/*    */   {
/* 21 */     this._StringMap = new HashMap();
/* 22 */     this.localFileName = ("/texts/" + setLanguageFile());
/* 23 */     readLanguageFile(paramApplet, this.localFileName);
/*    */   }
/*    */ 
/*    */   public void initStirng() {
/* 27 */     _OKText = (String)getStringMap().get("common.ok");
/* 28 */     _UploadText = (String)getStringMap().get("filetable.filetable_upload");
/*    */   }
/*    */ 
/*    */   public HashMap<String, String> getStringMap() {
/* 32 */     return this._StringMap;
/*    */   }
/*    */ 
/*    */   String setLanguageFile() {
/* 36 */     if (_locale.equalsIgnoreCase(new Locale("cs", "", "").getLanguage()))
/* 37 */       this.localFileName = "csy";
/* 38 */     else if (_locale.equalsIgnoreCase(new Locale("da", "", "").getLanguage()))
/* 39 */       this.localFileName = "dan";
/* 40 */     else if (_locale.equalsIgnoreCase(new Locale("nl", "", "").getLanguage()))
/* 41 */       this.localFileName = "nld";
/* 42 */     else if (_locale.equalsIgnoreCase(new Locale("fr", "", "").getLanguage()))
/* 43 */       this.localFileName = "fre";
/* 44 */     else if (_locale.equalsIgnoreCase(new Locale("de", "", "").getLanguage()))
/* 45 */       this.localFileName = "ger";
/* 46 */     else if (_locale.equalsIgnoreCase(new Locale("hu", "", "").getLanguage()))
/* 47 */       this.localFileName = "hun";
/* 48 */     else if (_locale.equalsIgnoreCase(new Locale("it", "", "").getLanguage()))
/* 49 */       this.localFileName = "ita";
/* 50 */     else if (_locale.equalsIgnoreCase(new Locale("ja", "", "").getLanguage()))
/* 51 */       this.localFileName = "jpn";
/* 52 */     else if (_locale.equalsIgnoreCase(new Locale("ko", "", "").getLanguage()))
/* 53 */       this.localFileName = "krn";
/* 54 */     else if ((_locale.equalsIgnoreCase(new Locale("no", "", "").getLanguage())) || (_locale.equalsIgnoreCase(new Locale("nn", "", "").getLanguage())) || (_locale.equalsIgnoreCase(new Locale("nb", "", "").getLanguage())))
/*    */     {
/* 57 */       this.localFileName = "nor";
/* 58 */     } else if (_locale.equalsIgnoreCase(new Locale("pl", "", "").getLanguage()))
/* 59 */       this.localFileName = "plk";
/* 60 */     else if (_locale.equalsIgnoreCase(new Locale("pt", "", "").getLanguage())) {
/* 61 */       if (_country.equalsIgnoreCase("br"))
/* 62 */         this.localFileName = "ptb";
/*    */       else
/* 64 */         this.localFileName = "ptg";
/*    */     }
/* 66 */     else if (_locale.equalsIgnoreCase(new Locale("ru", "", "").getLanguage()))
/* 67 */       this.localFileName = "rus";
/* 68 */     else if (_locale.equalsIgnoreCase(new Locale("es", "", "").getLanguage()))
/* 69 */       this.localFileName = "spn";
/* 70 */     else if (_locale.equalsIgnoreCase(new Locale("sv", "", "").getLanguage()))
/* 71 */       this.localFileName = "sve";
/* 72 */     else if (_locale.equalsIgnoreCase(new Locale("tr", "", "").getLanguage()))
/* 73 */       this.localFileName = "trk";
/* 74 */     else if (_locale.equalsIgnoreCase(new Locale("zh", "", "").getLanguage())) {
/* 75 */       if ((_country.equalsIgnoreCase("cn")) || (_country.equalsIgnoreCase("sg")))
/* 76 */         this.localFileName = "chs";
/*    */       else
/* 78 */         this.localFileName = "cht";
/*    */     }
/*    */     else {
/* 81 */       this.localFileName = "enu";
/*    */     }
/* 83 */     return this.localFileName;
/*    */   }
/*    */ 
/*    */   private void readLanguageFile(Applet paramApplet, String paramString) {
/* 87 */     BufferedReader localBufferedReader = null;
/*    */     try {
/* 89 */       localBufferedReader = new BufferedReader(new InputStreamReader(paramApplet.getClass().getResourceAsStream(paramString), "UTF8"));
/*    */       String str1;
/* 92 */       while ((str1 = localBufferedReader.readLine()) != null) {
/* 93 */         if (str1.startsWith("#")) {
/*    */           continue;
/*    */         }
/* 96 */         int i = str1.indexOf("=");
/* 97 */         if (i <= 0) {
/*    */           continue;
/*    */         }
/* 100 */         String str2 = str1.substring(0, i).trim().toLowerCase();
/* 101 */         String str3 = str1.substring(i + 1);
/* 102 */         this._StringMap.put(str2, str3);
/*    */       }
/*    */     } catch (Exception localException3) {
Exception localException2 = null;
/* 105 */       FileStationHandler.log(localException2);
/*    */     } finally {
/*    */       try {
/* 108 */         if (localBufferedReader != null)
/* 109 */           localBufferedReader.close();
/*    */       } catch (Exception localException4) {
/* 111 */         FileStationHandler.log(localException4);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     ui.UIString
 * JD-Core Version:    0.6.0
 */