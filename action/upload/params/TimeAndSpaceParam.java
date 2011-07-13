/*    */ package action.upload.params;
/*    */ 
/*    */ public class TimeAndSpaceParam
/*    */ {
/*    */   private long _TotalBytes;
/*    */   private long _UploadedBytes;
/*    */   private long _StartTime;
/*    */   private double _Duration;
/*    */   private double _LeftTime;
/*    */   private double _Velocity;
/*    */ 
/*    */   public TimeAndSpaceParam()
/*    */   {
/* 15 */     setTimeAndSpaceParam();
/*    */   }
/*    */ 
/*    */   public void setTaskTotalBytes(long paramLong) {
/* 19 */     this._TotalBytes = paramLong;
/*    */   }
/*    */ 
/*    */   public void setUploadStartTime(long paramLong) {
/* 23 */     this._StartTime = paramLong;
/*    */   }
/*    */ 
/*    */   public TimeAndSpaceParam UpdateTimeAndSpace(long paramLong) {
/* 27 */     this._UploadedBytes += paramLong;
/* 28 */     this._Duration = ((System.currentTimeMillis() - this._StartTime) / 1000.0D);
/* 29 */     this._Velocity = (0.0D < this._Duration ? this._UploadedBytes / this._Duration : 0.0D);
/*    */ 
/* 31 */     if (0L == this._UploadedBytes)
/*    */     {
/* 33 */       this._LeftTime = -1.0D;
/*    */     } else {
/* 35 */       this._LeftTime = (this._Duration * (this._TotalBytes - this._UploadedBytes) / this._UploadedBytes);
/* 36 */       if ((Double.valueOf(this._LeftTime).isInfinite()) || (Double.valueOf(this._LeftTime).isNaN()))
/*    */       {
/* 38 */         this._LeftTime = -1.0D;
/*    */       }
/*    */     }
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   public double getVelocity() {
/* 45 */     return this._Velocity;
/*    */   }
/*    */ 
/*    */   public double getLeftTime() {
/* 49 */     return this._LeftTime;
/*    */   }
/*    */ 
/*    */   public long getUploadBytes() {
/* 53 */     return this._UploadedBytes;
/*    */   }
/*    */ 
/*    */   public void success() {
/* 57 */     this._UploadedBytes = this._TotalBytes;
/* 58 */     this._LeftTime = 0.0D;
/* 59 */     this._Velocity = 0.0D;
/*    */   }
/*    */ 
/*    */   public void stop() {
/* 63 */     this._LeftTime = 0.0D;
/* 64 */     this._Velocity = 0.0D;
/*    */   }
/*    */ 
/*    */   public void setTimeAndSpaceParam() {
/* 68 */     this._TotalBytes = 0L;
/* 69 */     this._UploadedBytes = 0L;
/* 70 */     this._StartTime = 0L;
/* 71 */     this._Duration = 0.0D;
/* 72 */     this._LeftTime = -1.0D;
/* 73 */     this._Velocity = 0.0D;
/*    */   }
/*    */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     action.upload.params.TimeAndSpaceParam
 * JD-Core Version:    0.6.0
 */