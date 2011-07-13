/*    */ package org.json;
/*    */ 
/*    */ public class JSONException extends Exception
/*    */ {
/*    */   private Throwable cause;
/*    */ 
/*    */   public JSONException(String paramString)
/*    */   {
/* 16 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public JSONException(Throwable paramThrowable) {
/* 20 */     super(paramThrowable.getMessage());
/* 21 */     this.cause = paramThrowable;
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 25 */     return this.cause;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     org.json.JSONException
 * JD-Core Version:    0.6.0
 */