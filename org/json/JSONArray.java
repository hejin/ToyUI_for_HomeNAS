/*     */ package org.json;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
import java.util.Map;
/*     */ 
/*     */ public class JSONArray
/*     */ {
/*     */   private ArrayList myArrayList;
/*     */ 
/*     */   public JSONArray()
/*     */   {
/*  96 */     this.myArrayList = new ArrayList();
/*     */   }
/*     */ 
/*     */   public JSONArray(JSONTokener paramJSONTokener)
/*     */     throws JSONException
/*     */   {
/* 105 */     this();
/* 106 */     int i = paramJSONTokener.nextClean();
/*     */     char c;
/* 108 */     if (i == 91)
/* 109 */       c = ']';
/* 110 */     else if (i == 40)
/* 111 */       c = ')';
/*     */     else {
/* 113 */       throw paramJSONTokener.syntaxError("A JSONArray text must start with '['");
/*     */     }
/* 115 */     if (paramJSONTokener.nextClean() == ']') {
/* 116 */       return;
/*     */     }
/* 118 */     paramJSONTokener.back();
/*     */     while (true) {
/* 120 */       if (paramJSONTokener.nextClean() == ',') {
/* 121 */         paramJSONTokener.back();
/* 122 */         this.myArrayList.add(null);
/*     */       } else {
/* 124 */         paramJSONTokener.back();
/* 125 */         this.myArrayList.add(paramJSONTokener.nextValue());
/*     */       }
/* 127 */       i = paramJSONTokener.nextClean();
/* 128 */       switch (i) {
/*     */       case 44:
/*     */       case 59:
/* 131 */         if (paramJSONTokener.nextClean() == ']') {
/* 132 */           return;
/*     */         }
/* 134 */         paramJSONTokener.back();
/*     */       case 41:
/*     */       case 93:
/*     */       }
/*     */     }
			
/* 138 */   //  if (c != i) {
/* 139 */   //    throw paramJSONTokener.syntaxError("Expected a '" + new Character(c) + "'");
/*     */	//  }
			
/* 141 */   //  return;
              
/*     */ 
/* 143 */    // throw paramJSONTokener.syntaxError("Expected a ',' or ']'");
/*     */   }
/*     */ 
/*     */   public JSONArray(String paramString)
/*     */     throws JSONException
/*     */   {
/* 157 */     this(new JSONTokener(paramString));
/*     */   }
/*     */ 
/*     */   public JSONArray(Collection paramCollection)
/*     */   {
/* 166 */     this.myArrayList = (paramCollection == null ? new ArrayList() : new ArrayList(paramCollection));
/*     */   }
/*     */ 
/*     */   public JSONArray(Collection paramCollection, boolean paramBoolean)
/*     */   {
/* 179 */     this.myArrayList = new ArrayList();
/* 180 */     if (paramCollection != null) {
/* 181 */       Iterator localIterator = paramCollection.iterator();
/* 182 */       while (localIterator.hasNext()) {
/* 183 */         Object localObject = localIterator.next();
/* 184 */         if ((localObject instanceof Map))
/* 185 */           this.myArrayList.add(new JSONObject((Map)localObject, paramBoolean));
/* 186 */         else if (!JSONObject.isStandardProperty(localObject.getClass()))
/* 187 */           this.myArrayList.add(new JSONObject(localObject, paramBoolean));
/*     */         else
/* 189 */           this.myArrayList.add(localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public JSONArray(Object paramObject)
/*     */     throws JSONException
/*     */   {
/* 201 */     this();
/* 202 */     if (paramObject.getClass().isArray()) {
/* 203 */       int i = Array.getLength(paramObject);
/* 204 */       for (int j = 0; j < i; j++)
/* 205 */         put(Array.get(paramObject, j));
/*     */     }
/*     */     else {
/* 208 */       throw new JSONException("JSONArray initial value should be a string or collection or array.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public JSONArray(Object paramObject, boolean paramBoolean)
/*     */     throws JSONException
/*     */   {
/* 219 */     this();
/* 220 */     if (paramObject.getClass().isArray()) {
/* 221 */       int i = Array.getLength(paramObject);
/* 222 */       for (int j = 0; j < i; j++) {
/* 223 */         Object localObject = Array.get(paramObject, j);
/* 224 */         if (JSONObject.isStandardProperty(localObject.getClass()))
/* 225 */           this.myArrayList.add(localObject);
/*     */         else
/* 227 */           this.myArrayList.add(new JSONObject(localObject, paramBoolean));
/*     */       }
/*     */     }
/*     */     else {
/* 231 */       throw new JSONException("JSONArray initial value should be a string or collection or array.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 245 */     Object localObject = opt(paramInt);
/* 246 */     if (localObject == null) {
/* 247 */       throw new JSONException("JSONArray[" + paramInt + "] not found.");
/*     */     }
/* 249 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean getBoolean(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 263 */     Object localObject = get(paramInt);
/* 264 */     if ((localObject.equals(Boolean.FALSE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("false"))))
/*     */     {
/* 267 */       return false;
/* 268 */     }if ((localObject.equals(Boolean.TRUE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("true"))))
/*     */     {
/* 271 */       return true;
/*     */     }
/* 273 */     throw new JSONException("JSONArray[" + paramInt + "] is not a Boolean.");
/*     */   }
/*     */ 
/*     */   public double getDouble(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 286 */     Object localObject = get(paramInt);
/*     */     try {
/* 288 */       return (localObject instanceof Number) ? ((Number)localObject).doubleValue() : Double.valueOf((String)localObject).doubleValue();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 292 */     throw new JSONException("JSONArray[" + paramInt + "] is not a number.");
/*     */   }
/*     */ 
/*     */   public int getInt(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 308 */     Object localObject = get(paramInt);
/* 309 */     return (localObject instanceof Number) ? ((Number)localObject).intValue() : (int)getDouble(paramInt);
/*     */   }
/*     */ 
/*     */   public JSONArray getJSONArray(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 322 */     Object localObject = get(paramInt);
/* 323 */     if ((localObject instanceof JSONArray)) {
/* 324 */       return (JSONArray)localObject;
/*     */     }
/* 326 */     throw new JSONException("JSONArray[" + paramInt + "] is not a JSONArray.");
/*     */   }
/*     */ 
/*     */   public JSONObject getJSONObject(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 339 */     Object localObject = get(paramInt);
/* 340 */     if ((localObject instanceof JSONObject)) {
/* 341 */       return (JSONObject)localObject;
/*     */     }
/* 343 */     throw new JSONException("JSONArray[" + paramInt + "] is not a JSONObject.");
/*     */   }
/*     */ 
/*     */   public long getLong(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 357 */     Object localObject = get(paramInt);
/* 358 */     return (localObject instanceof Number) ? ((Number)localObject).longValue() : (long)getDouble(paramInt);
/*     */   }
/*     */ 
/*     */   public String getString(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 370 */     return get(paramInt).toString();
/*     */   }
/*     */ 
/*     */   public boolean isNull(int paramInt)
/*     */   {
/* 380 */     return JSONObject.NULL.equals(opt(paramInt));
/*     */   }
/*     */ 
/*     */   public String join(String paramString)
/*     */     throws JSONException
/*     */   {
/* 393 */     int i = length();
/* 394 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 396 */     for (int j = 0; j < i; j++) {
/* 397 */       if (j > 0) {
/* 398 */         localStringBuffer.append(paramString);
/*     */       }
/* 400 */       localStringBuffer.append(JSONObject.valueToString(this.myArrayList.get(j)));
/*     */     }
/* 402 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 412 */     return this.myArrayList.size();
/*     */   }
/*     */ 
/*     */   public Object opt(int paramInt)
/*     */   {
/* 423 */     return (paramInt < 0) || (paramInt >= length()) ? null : this.myArrayList.get(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean optBoolean(int paramInt)
/*     */   {
/* 437 */     return optBoolean(paramInt, false);
/*     */   }
/*     */ 
/*     */   public boolean optBoolean(int paramInt, boolean paramBoolean)
/*     */   {
/*     */     try
/*     */     {
/* 452 */       return getBoolean(paramInt); } catch (Exception localException) {
/*     */     }
/* 454 */     return paramBoolean;
/*     */   }
/*     */ 
/*     */   public double optDouble(int paramInt)
/*     */   {
/* 468 */     return optDouble(paramInt, (0.0D / 0.0D));
/*     */   }
/*     */ 
/*     */   public double optDouble(int paramInt, double paramDouble)
/*     */   {
/*     */     try
/*     */     {
/* 483 */       return getDouble(paramInt); } catch (Exception localException) {
/*     */     }
/* 485 */     return paramDouble;
/*     */   }
/*     */ 
/*     */   public int optInt(int paramInt)
/*     */   {
/* 499 */     return optInt(paramInt, 0);
/*     */   }
/*     */ 
/*     */   public int optInt(int paramInt1, int paramInt2)
/*     */   {
/*     */     try
/*     */     {
/* 513 */       return getInt(paramInt1); } catch (Exception localException) {
/*     */     }
/* 515 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public JSONArray optJSONArray(int paramInt)
/*     */   {
/* 527 */     Object localObject = opt(paramInt);
/* 528 */     return (localObject instanceof JSONArray) ? (JSONArray)localObject : null;
/*     */   }
/*     */ 
/*     */   public JSONObject optJSONObject(int paramInt)
/*     */   {
/* 541 */     Object localObject = opt(paramInt);
/* 542 */     return (localObject instanceof JSONObject) ? (JSONObject)localObject : null;
/*     */   }
/*     */ 
/*     */   public long optLong(int paramInt)
/*     */   {
/* 555 */     return optLong(paramInt, 0L);
/*     */   }
/*     */ 
/*     */   public long optLong(int paramInt, long paramLong)
/*     */   {
/*     */     try
/*     */     {
/* 569 */       return getLong(paramInt); } catch (Exception localException) {
/*     */     }
/* 571 */     return paramLong;
/*     */   }
/*     */ 
/*     */   public String optString(int paramInt)
/*     */   {
/* 585 */     return optString(paramInt, "");
/*     */   }
/*     */ 
/*     */   public String optString(int paramInt, String paramString)
/*     */   {
/* 598 */     Object localObject = opt(paramInt);
/* 599 */     return localObject != null ? localObject.toString() : paramString;
/*     */   }
/*     */ 
/*     */   public JSONArray put(boolean paramBoolean)
/*     */   {
/* 610 */     put(paramBoolean ? Boolean.TRUE : Boolean.FALSE);
/* 611 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(Collection paramCollection)
/*     */   {
/* 622 */     put(new JSONArray(paramCollection));
/* 623 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(double paramDouble)
/*     */     throws JSONException
/*     */   {
/* 635 */     Double localDouble = new Double(paramDouble);
/* 636 */     JSONObject.testValidity(localDouble);
/* 637 */     put(localDouble);
/* 638 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt)
/*     */   {
/* 649 */     put(new Integer(paramInt));
/* 650 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(long paramLong)
/*     */   {
/* 661 */     put(new Long(paramLong));
/* 662 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(Map paramMap)
/*     */   {
/* 673 */     put(new JSONObject(paramMap));
/* 674 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(Object paramObject)
/*     */   {
/* 686 */     this.myArrayList.add(paramObject);
/* 687 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt, boolean paramBoolean)
/*     */     throws JSONException
/*     */   {
/* 701 */     put(paramInt, paramBoolean ? Boolean.TRUE : Boolean.FALSE);
/* 702 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt, Collection paramCollection)
/*     */     throws JSONException
/*     */   {
/* 716 */     put(paramInt, new JSONArray(paramCollection));
/* 717 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt, double paramDouble)
/*     */     throws JSONException
/*     */   {
/* 732 */     put(paramInt, new Double(paramDouble));
/* 733 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt1, int paramInt2)
/*     */     throws JSONException
/*     */   {
/* 747 */     put(paramInt1, new Integer(paramInt2));
/* 748 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt, long paramLong)
/*     */     throws JSONException
/*     */   {
/* 762 */     put(paramInt, new Long(paramLong));
/* 763 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt, Map paramMap)
/*     */     throws JSONException
/*     */   {
/* 777 */     put(paramInt, new JSONObject(paramMap));
/* 778 */     return this;
/*     */   }
/*     */ 
/*     */   public JSONArray put(int paramInt, Object paramObject)
/*     */     throws JSONException
/*     */   {
/* 795 */     JSONObject.testValidity(paramObject);
/* 796 */     if (paramInt < 0) {
/* 797 */       throw new JSONException("JSONArray[" + paramInt + "] not found.");
/*     */     }
/* 799 */     if (paramInt < length()) {
/* 800 */       this.myArrayList.set(paramInt, paramObject);
/*     */     } else {
/* 802 */       while (paramInt != length()) {
/* 803 */         put(JSONObject.NULL);
/*     */       }
/* 805 */       put(paramObject);
/*     */     }
/* 807 */     return this;
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt)
/*     */   {
/* 818 */     Object localObject = opt(paramInt);
/* 819 */     this.myArrayList.remove(paramInt);
/* 820 */     return localObject;
/*     */   }
/*     */ 
/*     */   public JSONObject toJSONObject(JSONArray paramJSONArray)
/*     */     throws JSONException
/*     */   {
/* 834 */     if ((paramJSONArray == null) || (paramJSONArray.length() == 0) || (length() == 0)) {
/* 835 */       return null;
/*     */     }
/* 837 */     JSONObject localJSONObject = new JSONObject();
/* 838 */     for (int i = 0; i < paramJSONArray.length(); i++) {
/* 839 */       localJSONObject.put(paramJSONArray.getString(i), opt(i));
/*     */     }
/* 841 */     return localJSONObject;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     try
/*     */     {
/* 858 */       return '[' + join(",") + ']'; } catch (Exception localException) {
/*     */     }
/* 860 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString(int paramInt)
/*     */     throws JSONException
/*     */   {
/* 877 */     return toString(paramInt, 0);
/*     */   }
/*     */ 
/*     */   String toString(int paramInt1, int paramInt2)
/*     */     throws JSONException
/*     */   {
/* 892 */     int i = length();
/* 893 */     if (i == 0) {
/* 894 */       return "[]";
/*     */     }
/*     */ 
/* 897 */     StringBuffer localStringBuffer = new StringBuffer("[");
/* 898 */     if (i == 1) {
/* 899 */       localStringBuffer.append(JSONObject.valueToString(this.myArrayList.get(0), paramInt1, paramInt2));
/*     */     }
/*     */     else {
/* 902 */       int k = paramInt2 + paramInt1;
/* 903 */       localStringBuffer.append('\n');
/* 904 */       for (int j = 0; j < i; j++) {
/* 905 */         if (j > 0) {
/* 906 */           localStringBuffer.append(",\n");
/*     */         }
/* 908 */         for (int m = 0; m < k; m++) {
/* 909 */           localStringBuffer.append(' ');
/*     */         }
/* 911 */         localStringBuffer.append(JSONObject.valueToString(this.myArrayList.get(j), paramInt1, k));
/*     */       }
/*     */ 
/* 914 */       localStringBuffer.append('\n');
/* 915 */       for (int j = 0; j < paramInt2; j++) {
/* 916 */         localStringBuffer.append(' ');
/*     */       }
/*     */     }
/* 919 */     localStringBuffer.append(']');
/* 920 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public Writer write(Writer paramWriter)
/*     */     throws JSONException
/*     */   {
/*     */     try
/*     */     {
/* 935 */       int i = 0;
/* 936 */       int j = length();
/*     */ 
/* 938 */       paramWriter.write(91);
/*     */ 
/* 940 */       for (int k = 0; k < j; k++) {
/* 941 */         if (i != 0) {
/* 942 */           paramWriter.write(44);
/*     */         }
/* 944 */         Object localObject = this.myArrayList.get(k);
/* 945 */         if ((localObject instanceof JSONObject))
/* 946 */           ((JSONObject)localObject).write(paramWriter);
/* 947 */         else if ((localObject instanceof JSONArray))
/* 948 */           ((JSONArray)localObject).write(paramWriter);
/*     */         else {
/* 950 */           paramWriter.write(JSONObject.valueToString(localObject));
/*     */         }
/* 952 */         i = 1;
/*     */       }
/* 954 */       paramWriter.write(93);
/* 955 */       return paramWriter; } catch (IOException localIOException) {
/*     */     }
String localIOException = null;
/* 957 */     throw new JSONException(localIOException);
/*     */   }
/*     */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     org.json.JSONArray
 * JD-Core Version:    0.6.0
 */