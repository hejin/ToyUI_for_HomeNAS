/*     */ package org.json;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ 
/*     */ public class JSONTokener
/*     */ {
/*     */   private int index;
/*     */   private Reader reader;
/*     */   private char lastChar;
/*     */   private boolean useLastChar;
/*     */ 
/*     */   public JSONTokener(Reader paramReader)
/*     */   {
/*  53 */     this.reader = (paramReader.markSupported() ? paramReader : new BufferedReader(paramReader));
/*     */ 
/*  55 */     this.useLastChar = false;
/*  56 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   public JSONTokener(String paramString)
/*     */   {
/*  66 */     this(new StringReader(paramString));
/*     */   }
/*     */ 
/*     */   public void back()
/*     */     throws JSONException
/*     */   {
/*  76 */     if ((this.useLastChar) || (this.index <= 0)) {
/*  77 */       throw new JSONException("Stepping back two steps is not supported");
/*     */     }
/*  79 */     this.index -= 1;
/*  80 */     this.useLastChar = true;
/*     */   }
/*     */ 
/*     */   public static int dehexchar(char paramChar)
/*     */   {
/*  92 */     if ((paramChar >= '0') && (paramChar <= '9')) {
/*  93 */       return paramChar - '0';
/*     */     }
/*  95 */     if ((paramChar >= 'A') && (paramChar <= 'F')) {
/*  96 */       return paramChar - '7';
/*     */     }
/*  98 */     if ((paramChar >= 'a') && (paramChar <= 'f')) {
/*  99 */       return paramChar - 'W';
/*     */     }
/* 101 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean more()
/*     */     throws JSONException
/*     */   {
/* 111 */     int i = next();
/* 112 */     if (i == 0) {
/* 113 */       return false;
/*     */     }
/* 115 */     back();
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   public char next()
/*     */     throws JSONException
/*     */   {
/* 126 */     if (this.useLastChar) {
/* 127 */       this.useLastChar = false;
/* 128 */       if (this.lastChar != 0) {
/* 129 */         this.index += 1;
/*     */       }
/* 131 */       return this.lastChar;
/*     */     }int i;
/*     */     try {
/* 135 */       i = this.reader.read();
/*     */     } catch (IOException localIOException) {
/* 137 */       throw new JSONException(localIOException);
/*     */     }
/*     */ 
/* 140 */     if (i <= 0) {
/* 141 */       this.lastChar = '\000';
/* 142 */       return '\000';
/*     */     }
/* 144 */     this.index += 1;
/* 145 */     this.lastChar = (char)i;
/* 146 */     return this.lastChar;
/*     */   }
/*     */ 
/*     */   public char next(char paramChar)
/*     */     throws JSONException
/*     */   {
/* 158 */     char c = next();
/* 159 */     if (c != paramChar) {
/* 160 */       throw syntaxError("Expected '" + paramChar + "' and instead saw '" + c + "'");
/*     */     }
/*     */ 
/* 163 */     return c;
/*     */   }
/*     */ 
/*     */   public String next(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 177 */     if (paramInt == 0) {
/* 178 */       return "";
/*     */     }
/*     */ 
/* 181 */     char[] arrayOfChar = new char[paramInt];
/* 182 */     int i = 0;
/*     */ 
/* 184 */     if (this.useLastChar) {
/* 185 */       this.useLastChar = false;
/* 186 */       arrayOfChar[0] = this.lastChar;
/* 187 */       i = 1;
/*     */     }
/*     */     try
/*     */     {
/*     */       int j;
/* 192 */       while ((i < paramInt) && ((j = this.reader.read(arrayOfChar, i, paramInt - i)) != -1))
/* 193 */         i += j;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 196 */       throw new JSONException(localIOException);
/*     */     }
/* 198 */     this.index += i;
/*     */ 
/* 200 */     if (i < paramInt) {
/* 201 */       throw syntaxError("Substring bounds error");
/*     */     }
/*     */ 
/* 204 */     this.lastChar = arrayOfChar[(paramInt - 1)];
/* 205 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public char nextClean()
/*     */     throws JSONException
/*     */   {
/*     */     while (true)
/*     */     {
/* 216 */       int i = next();
/* 217 */       if ((i == 0) || (i > 32))
/* 218 */         return (char)i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String nextString(char paramChar)
/*     */     throws JSONException
/*     */   {
/* 237 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */     while (true) {
/* 239 */       char c = next();
/* 240 */       switch (c) {
/*     */       case '\000':
/*     */       case '\n':
/*     */       case '\r':
/* 244 */         throw syntaxError("Unterminated string");
/*     */       case '\\':
/* 246 */         c = next();
/* 247 */         switch (c) {
/*     */         case 'b':
/* 249 */           localStringBuffer.append('\b');
/* 250 */           break;
/*     */         case 't':
/* 252 */           localStringBuffer.append('\t');
/* 253 */           break;
/*     */         case 'n':
/* 255 */           localStringBuffer.append('\n');
/* 256 */           break;
/*     */         case 'f':
/* 258 */           localStringBuffer.append('\f');
/* 259 */           break;
/*     */         case 'r':
/* 261 */           localStringBuffer.append('\r');
/* 262 */           break;
/*     */         case 'u':
/* 264 */           localStringBuffer.append((char)Integer.parseInt(next(4), 16));
/* 265 */           break;
/*     */         case '"':
/*     */         case '\'':
/*     */         case '/':
/*     */         case '\\':
/* 270 */           localStringBuffer.append(c);
/* 271 */           break;
/*     */         default:
/* 273 */           throw syntaxError("Illegal escape.");
/*     */         }
/*     */ 
/*     */       default:
/* 277 */         if (c == paramChar) {
/* 278 */           return localStringBuffer.toString();
/*     */         }
/* 280 */         localStringBuffer.append(c);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String nextTo(char paramChar)
/*     */     throws JSONException
/*     */   {
/* 293 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */     while (true) {
/* 295 */       char c = next();
/* 296 */       if ((c == paramChar) || (c == 0) || (c == '\n') || (c == '\r')) {
/* 297 */         if (c != 0) {
/* 298 */           back();
/*     */         }
/* 300 */         return localStringBuffer.toString().trim();
/*     */       }
/* 302 */       localStringBuffer.append(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String nextTo(String paramString)
/*     */     throws JSONException
/*     */   {
/* 315 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */     while (true) {
/* 317 */       char c = next();
/* 318 */       if ((paramString.indexOf(c) >= 0) || (c == 0) || (c == '\n') || (c == '\r'))
/*     */       {
/* 320 */         if (c != 0) {
/* 321 */           back();
/*     */         }
/* 323 */         return localStringBuffer.toString().trim();
/*     */       }
/* 325 */       localStringBuffer.append(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object nextValue()
/*     */     throws JSONException
/*     */   {
/* 338 */     char c = nextClean();
/*     */ 
/* 341 */     switch (c) {
/*     */     case '"':
/*     */     case '\'':
/* 344 */       return nextString(c);
/*     */     case '{':
/* 346 */       back();
/* 347 */       return new JSONObject(this);
/*     */     case '(':
/*     */     case '[':
/* 350 */       back();
/* 351 */       return new JSONArray(this);
/*     */     }
/*     */ 
/* 363 */     StringBuffer localStringBuffer = new StringBuffer();
/* 364 */     while ((c >= ' ') && (",:]}/\\\"[{;=#".indexOf(c) < 0)) {
/* 365 */       localStringBuffer.append(c);
/* 366 */       c = next();
/*     */     }
/* 368 */     back();
/*     */ 
/* 370 */     String str = localStringBuffer.toString().trim();
/* 371 */     if (str.equals("")) {
/* 372 */       throw syntaxError("Missing value");
/*     */     }
/* 374 */     return JSONObject.stringToValue(str);
/*     */   }
/*     */ 
/*     */   public char skipTo(char paramChar)
/*     */     throws JSONException
/*     */   {
/*     */     char c;
/*     */     try
/*     */     {
/* 388 */       int i = this.index;
/* 389 */       this.reader.mark(2147483647);
/*     */       do {
/* 391 */         c = next();
/* 392 */         if (c == 0) {
/* 393 */           this.reader.reset();
/* 394 */           this.index = i;
/* 395 */           return c;
/*     */         }
/*     */       }
/* 397 */       while (c != paramChar);
/*     */     } catch (IOException localIOException) {
/* 399 */       throw new JSONException(localIOException);
/*     */     }
/*     */ 
/* 402 */     back();
/* 403 */     return c;
/*     */   }
/*     */ 
/*     */   public JSONException syntaxError(String paramString)
/*     */   {
/* 413 */     return new JSONException(paramString + toString());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 423 */     return " at character " + this.index;
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     org.json.JSONTokener
 * JD-Core Version:    0.6.0
 */