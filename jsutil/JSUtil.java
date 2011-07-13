/*    */ package jsutil;
/*    */ 
/*    */ import action.FileStationHandler;
/*    */ import action.upload.params.UploadTaskParam;
/*    */ import java.applet.Applet;
/*    */ import jsonutil.JSONUtil;
/*    */ import netscape.javascript.JSObject;
/*    */ 
/*    */ public class JSUtil
/*    */ {
/* 16 */   private static String JSNAME = "SYNO.FileStation.instance().getUploadInstance().";
/*    */ 
/*    */   public static boolean evalJSON(Applet paramApplet, String paramString, UploadTaskParam paramUploadTaskParam)
/*    */   {
/*    */     try {
/* 21 */       if (paramUploadTaskParam != null) {
/* 22 */         JSObject.getWindow(paramApplet).eval(JSNAME + paramString + "(" + JSONUtil.setUploadTaskParam(paramUploadTaskParam) + ");");
/*    */       }
/*    */ 
/* 26 */       return true;
/*    */     }
/*    */     catch (Exception localException) {
/* 29 */       FileStationHandler.log(localException);
/* 30 */     }return false;
/*    */   }
/*    */ 
/*    */   public static boolean eval(Applet paramApplet, String paramString, Object paramObject)
/*    */   {
/*    */     try {
/* 36 */       JSObject.getWindow(paramApplet).eval(JSNAME + paramString + "(" + paramObject + ");");
/* 37 */       return true;
/*    */     }
/*    */     catch (Exception localException) {
/* 40 */       FileStationHandler.log(localException);
/* 41 */     }return false;
/*    */   }
/*    */ 
/*    */   public static boolean eval(Applet paramApplet, String paramString1, String paramString2, Object paramObject)
/*    */   {
/*    */     try {
/* 47 */       JSObject.getWindow(paramApplet).eval(paramString1 + paramString2 + "(" + paramObject + ");");
/* 48 */       return true;
/*    */     }
/*    */     catch (Exception localException) {
/* 51 */       FileStationHandler.log(localException);
/* 52 */     }return false;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     jsutil.JSUtil
 * JD-Core Version:    0.6.0
 */