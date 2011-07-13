/*      */ package org.json;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
import java.util.TreeSet;
/*      */ 
/*      */ public class JSONObject
/*      */ {
/*      */   private Map map;
/*  142 */   public static final Object NULL = new Null(); //Todo
/*      */ 
/*      */   public JSONObject()
/*      */   {
/*  149 */     this.map = new HashMap();
/*      */   }
/*      */ 
/*      */   public JSONObject(JSONObject paramJSONObject, String[] paramArrayOfString)
/*      */     throws JSONException
/*      */   {
/*  162 */     this();
/*  163 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*  164 */       putOnce(paramArrayOfString[i], paramJSONObject.opt(paramArrayOfString[i]));
/*      */   }
/*      */ 
/*      */   public JSONObject(JSONTokener paramJSONTokener)
/*      */     throws JSONException
/*      */   {
/*  176 */     this();
/*      */ 
/*  180 */     if (paramJSONTokener.nextClean() != '{')
/*  181 */       throw paramJSONTokener.syntaxError("A JSONObject text must begin with '{'");
/*      */     while (true)
/*      */     {
/*  184 */       int i = paramJSONTokener.nextClean();
/*  185 */       switch (i) {
/*      */       case 0:
/*  187 */         throw paramJSONTokener.syntaxError("A JSONObject text must end with '}'");
/*      */       case 125:
/*  189 */         return;
/*      */       }
/*  191 */       paramJSONTokener.back();
/*  192 */       String str = paramJSONTokener.nextValue().toString();
/*      */ 
/*  199 */       i = paramJSONTokener.nextClean();
/*  200 */       if (i == 61) {
/*  201 */         if (paramJSONTokener.next() != '>')
/*  202 */           paramJSONTokener.back();
/*      */       }
/*  204 */       else if (i != 58) {
/*  205 */         throw paramJSONTokener.syntaxError("Expected a ':' after a key");
/*      */       }
/*  207 */       putOnce(str, paramJSONTokener.nextValue());
/*      */ 
/*  213 */       switch (paramJSONTokener.nextClean()) {
/*      */       case ',':
/*      */       case ';':
/*  216 */         if (paramJSONTokener.nextClean() == '}') {
/*  217 */           return;
/*      */         }
/*  219 */         paramJSONTokener.back();
/*      */       case '}':
/*      */       }
/*      */     }   }
/*      */ 
/*      */   public JSONObject(Map paramMap)
/*      */   {
/*  237 */     this.map = (paramMap == null ? new HashMap() : paramMap);
/*      */   }
/*      */ 
/*      */   public JSONObject(Map paramMap, boolean paramBoolean)
/*      */   {
/*  250 */     this.map = new HashMap();
/*  251 */     if (paramMap != null) {
/*  252 */       Iterator localIterator = paramMap.entrySet().iterator();
/*  253 */       while (localIterator.hasNext()) {
/*  254 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  255 */         if (isStandardProperty(localEntry.getValue().getClass()))
/*  256 */           this.map.put(localEntry.getKey(), localEntry.getValue());
/*      */         else
/*  258 */           this.map.put(localEntry.getKey(), new JSONObject(localEntry.getValue(), paramBoolean));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public JSONObject(Object paramObject)
/*      */   {
/*  286 */     this();
/*  287 */     populateInternalMap(paramObject, false);
/*      */   }
/*      */ 
/*      */   public JSONObject(Object paramObject, boolean paramBoolean)
/*      */   {
/*  308 */     this();
/*  309 */     populateInternalMap(paramObject, paramBoolean);
/*      */   }
/*      */ 
/*      */   private void populateInternalMap(Object paramObject, boolean paramBoolean) {
/*  313 */     Class localClass = paramObject.getClass();
/*      */ 
/*  317 */     if (localClass.getClassLoader() == null) {
/*  318 */       paramBoolean = false;
/*      */     }
/*      */ 
/*  321 */     Method[] arrayOfMethod = paramBoolean ? localClass.getMethods() : localClass.getDeclaredMethods();
/*      */ 
/*  323 */     for (int i = 0; i < arrayOfMethod.length; i++)
/*      */       try {
/*  325 */         Method localMethod = arrayOfMethod[i];
/*  326 */         if (Modifier.isPublic(localMethod.getModifiers())) {
/*  327 */           String str1 = localMethod.getName();
/*  328 */           String str2 = "";
/*  329 */           if (str1.startsWith("get"))
/*  330 */             str2 = str1.substring(3);
/*  331 */           else if (str1.startsWith("is")) {
/*  332 */             str2 = str1.substring(2);
/*      */           }
/*  334 */           if ((str2.length() > 0) && (Character.isUpperCase(str2.charAt(0))) && (localMethod.getParameterTypes().length == 0))
/*      */           {
/*  337 */             if (str2.length() == 1)
/*  338 */               str2 = str2.toLowerCase();
/*  339 */             else if (!Character.isUpperCase(str2.charAt(1))) {
/*  340 */               str2 = str2.substring(0, 1).toLowerCase() + str2.substring(1);
/*      */             }
/*      */ 
/*  344 */             Object localObject = localMethod.invoke(paramObject, (Object[])null);
/*  345 */             if (localObject == null)
/*  346 */               this.map.put(str2, NULL);
/*  347 */             else if (localObject.getClass().isArray())
/*  348 */               this.map.put(str2, new JSONArray(localObject, paramBoolean));
/*  349 */             else if ((localObject instanceof Collection))
/*  350 */               this.map.put(str2, new JSONArray((Collection)localObject, paramBoolean));
/*  351 */             else if ((localObject instanceof Map))
/*  352 */               this.map.put(str2, new JSONObject((Map)localObject, paramBoolean));
/*  353 */             else if (isStandardProperty(localObject.getClass())) {
/*  354 */               this.map.put(str2, localObject);
/*      */             }
/*  356 */             else if ((localObject.getClass().getPackage().getName().startsWith("java")) || (localObject.getClass().getClassLoader() == null))
/*      */             {
/*  358 */               this.map.put(str2, localObject.toString());
/*      */             }
/*  360 */             else this.map.put(str2, new JSONObject(localObject, paramBoolean));
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*  366 */         throw new RuntimeException(localException);
/*      */       }
/*      */   }
/*      */ 
/*      */   static boolean isStandardProperty(Class paramClass)
/*      */   {
/*  373 */     return (paramClass.isPrimitive()) || (paramClass.isAssignableFrom(Byte.class)) || (paramClass.isAssignableFrom(Short.class)) || (paramClass.isAssignableFrom(Integer.class)) || (paramClass.isAssignableFrom(Long.class)) || (paramClass.isAssignableFrom(Float.class)) || (paramClass.isAssignableFrom(Double.class)) || (paramClass.isAssignableFrom(Character.class)) || (paramClass.isAssignableFrom(String.class)) || (paramClass.isAssignableFrom(Boolean.class));
/*      */   }
/*      */ 
/*      */   public JSONObject(Object paramObject, String[] paramArrayOfString)
/*      */   {
/*  398 */     this();
/*  399 */     Class localClass = paramObject.getClass();
/*  400 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  401 */       String str = paramArrayOfString[i];
/*      */       try {
/*  403 */         putOpt(str, localClass.getField(str).get(paramObject));
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public JSONObject(String paramString)
/*      */     throws JSONException
/*      */   {
/*  421 */     this(new JSONTokener(paramString));
/*      */   }
/*      */ 
/*      */   public JSONObject accumulate(String paramString, Object paramObject)
/*      */     throws JSONException
/*      */   {
/*  439 */     testValidity(paramObject);
/*  440 */     Object localObject = opt(paramString);
/*  441 */     if (localObject == null) {
/*  442 */       put(paramString, (paramObject instanceof JSONArray) ? new JSONArray().put(paramObject) : paramObject);
/*      */     }
/*  445 */     else if ((localObject instanceof JSONArray))
/*  446 */       ((JSONArray)localObject).put(paramObject);
/*      */     else {
/*  448 */       put(paramString, new JSONArray().put(localObject).put(paramObject));
/*      */     }
/*  450 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject append(String paramString, Object paramObject)
/*      */     throws JSONException
/*      */   {
/*  467 */     testValidity(paramObject);
/*  468 */     Object localObject = opt(paramString);
/*  469 */     if (localObject == null)
/*  470 */       put(paramString, new JSONArray().put(paramObject));
/*  471 */     else if ((localObject instanceof JSONArray))
/*  472 */       put(paramString, ((JSONArray)localObject).put(paramObject));
/*      */     else {
/*  474 */       throw new JSONException("JSONObject[" + paramString + "] is not a JSONArray.");
/*      */     }
/*      */ 
/*  477 */     return this;
/*      */   }
/*      */ 
/*      */   public static String doubleToString(double paramDouble)
/*      */   {
/*  488 */     if ((Double.isInfinite(paramDouble)) || (Double.isNaN(paramDouble))) {
/*  489 */       return "null";
/*      */     }
/*      */ 
/*  494 */     String str = Double.toString(paramDouble);
/*  495 */     if ((str.indexOf('.') > 0) && (str.indexOf('e') < 0) && (str.indexOf('E') < 0)) {
/*  496 */       while (str.endsWith("0")) {
/*  497 */         str = str.substring(0, str.length() - 1);
/*      */       }
/*  499 */       if (str.endsWith(".")) {
/*  500 */         str = str.substring(0, str.length() - 1);
/*      */       }
/*      */     }
/*  503 */     return str;
/*      */   }
/*      */ 
/*      */   public Object get(String paramString)
/*      */     throws JSONException
/*      */   {
/*  515 */     Object localObject = opt(paramString);
/*  516 */     if (localObject == null) {
/*  517 */       throw new JSONException("JSONObject[" + quote(paramString) + "] not found.");
/*      */     }
/*      */ 
/*  520 */     return localObject;
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(String paramString)
/*      */     throws JSONException
/*      */   {
/*  533 */     Object localObject = get(paramString);
/*  534 */     if ((localObject.equals(Boolean.FALSE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("false"))))
/*      */     {
/*  537 */       return false;
/*  538 */     }if ((localObject.equals(Boolean.TRUE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("true"))))
/*      */     {
/*  541 */       return true;
/*      */     }
/*  543 */     throw new JSONException("JSONObject[" + quote(paramString) + "] is not a Boolean.");
/*      */   }
/*      */ 
/*      */   public double getDouble(String paramString)
/*      */     throws JSONException
/*      */   {
/*  556 */     Object localObject = get(paramString);
/*      */     try {
/*  558 */       return (localObject instanceof Number) ? ((Number)localObject).doubleValue() : Double.valueOf((String)localObject).doubleValue();
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  562 */     throw new JSONException("JSONObject[" + quote(paramString) + "] is not a number.");
/*      */   }
/*      */ 
/*      */   public int getInt(String paramString)
/*      */     throws JSONException
/*      */   {
/*  578 */     Object localObject = get(paramString);
/*  579 */     return (localObject instanceof Number) ? ((Number)localObject).intValue() : (int)getDouble(paramString);
/*      */   }
/*      */ 
/*      */   public JSONArray getJSONArray(String paramString)
/*      */     throws JSONException
/*      */   {
/*  593 */     Object localObject = get(paramString);
/*  594 */     if ((localObject instanceof JSONArray)) {
/*  595 */       return (JSONArray)localObject;
/*      */     }
/*  597 */     throw new JSONException("JSONObject[" + quote(paramString) + "] is not a JSONArray.");
/*      */   }
/*      */ 
/*      */   public JSONObject getJSONObject(String paramString)
/*      */     throws JSONException
/*      */   {
/*  611 */     Object localObject = get(paramString);
/*  612 */     if ((localObject instanceof JSONObject)) {
/*  613 */       return (JSONObject)localObject;
/*      */     }
/*  615 */     throw new JSONException("JSONObject[" + quote(paramString) + "] is not a JSONObject.");
/*      */   }
/*      */ 
/*      */   public long getLong(String paramString)
/*      */     throws JSONException
/*      */   {
/*  630 */     Object localObject = get(paramString);
/*  631 */     return (localObject instanceof Number) ? ((Number)localObject).longValue() : (long)getDouble(paramString);
/*      */   }
/*      */ 
/*      */   public static String[] getNames(JSONObject paramJSONObject)
/*      */   {
/*  642 */     int i = paramJSONObject.length();
/*  643 */     if (i == 0) {
/*  644 */       return null;
/*      */     }
/*  646 */     Iterator localIterator = paramJSONObject.keys();
/*  647 */     String[] arrayOfString = new String[i];
/*  648 */     int j = 0;
/*  649 */     while (localIterator.hasNext()) {
/*  650 */       arrayOfString[j] = ((String)localIterator.next());
/*  651 */       j++;
/*      */     }
/*  653 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public static String[] getNames(Object paramObject)
/*      */   {
/*  663 */     if (paramObject == null) {
/*  664 */       return null;
/*      */     }
/*  666 */     Class localClass = paramObject.getClass();
/*  667 */     Field[] arrayOfField = localClass.getFields();
/*  668 */     int i = arrayOfField.length;
/*  669 */     if (i == 0) {
/*  670 */       return null;
/*      */     }
/*  672 */     String[] arrayOfString = new String[i];
/*  673 */     for (int j = 0; j < i; j++) {
/*  674 */       arrayOfString[j] = arrayOfField[j].getName();
/*      */     }
/*  676 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public String getString(String paramString)
/*      */     throws JSONException
/*      */   {
/*  688 */     return get(paramString).toString();
/*      */   }
/*      */ 
/*      */   public boolean has(String paramString)
/*      */   {
/*  706 */     return this.map.containsKey(paramString);
/*      */   }
/*      */ 
/*      */   public boolean isNull(String paramString)
/*      */   {
/*  718 */     return NULL.equals(opt(paramString));
/*      */   }
/*      */ 
/*      */   public Iterator keys()
/*      */   {
/*  728 */     return this.map.keySet().iterator();
/*      */   }
/*      */ 
/*      */   public int length()
/*      */   {
/*  738 */     return this.map.size();
/*      */   }
/*      */ 
/*      */   public JSONArray names()
/*      */   {
/*  749 */     JSONArray localJSONArray = new JSONArray();
/*  750 */     Iterator localIterator = keys();
/*  751 */     while (localIterator.hasNext()) {
/*  752 */       localJSONArray.put(localIterator.next());
/*      */     }
/*  754 */     return localJSONArray.length() == 0 ? null : localJSONArray;
/*      */   }
/*      */ 
/*      */   public static String numberToString(Number paramNumber)
/*      */     throws JSONException
/*      */   {
/*  765 */     if (paramNumber == null) {
/*  766 */       throw new JSONException("Null pointer");
/*      */     }
/*  768 */     testValidity(paramNumber);
/*      */ 
/*  772 */     String str = paramNumber.toString();
/*  773 */     if ((str.indexOf('.') > 0) && (str.indexOf('e') < 0) && (str.indexOf('E') < 0)) {
/*  774 */       while (str.endsWith("0")) {
/*  775 */         str = str.substring(0, str.length() - 1);
/*      */       }
/*  777 */       if (str.endsWith(".")) {
/*  778 */         str = str.substring(0, str.length() - 1);
/*      */       }
/*      */     }
/*  781 */     return str;
/*      */   }
/*      */ 
/*      */   public Object opt(String paramString)
/*      */   {
/*  791 */     return paramString == null ? null : this.map.get(paramString);
/*      */   }
/*      */ 
/*      */   public boolean optBoolean(String paramString)
/*      */   {
/*  804 */     return optBoolean(paramString, false);
/*      */   }
/*      */ 
/*      */   public boolean optBoolean(String paramString, boolean paramBoolean)
/*      */   {
/*      */     try
/*      */     {
/*  819 */       return getBoolean(paramString); } catch (Exception localException) {
/*      */     }
/*  821 */     return paramBoolean;
/*      */   }
/*      */ 
/*      */   public JSONObject put(String paramString, Collection paramCollection)
/*      */     throws JSONException
/*      */   {
/*  835 */     put(paramString, new JSONArray(paramCollection));
/*  836 */     return this;
/*      */   }
/*      */ 
/*      */   public double optDouble(String paramString)
/*      */   {
/*  850 */     return optDouble(paramString, (0.0D / 0.0D));
/*      */   }
/*      */ 
/*      */   public double optDouble(String paramString, double paramDouble)
/*      */   {
/*      */     try
/*      */     {
/*  866 */       Object localObject = opt(paramString);
/*  867 */       return (localObject instanceof Number) ? ((Number)localObject).doubleValue() : new Double((String)localObject).doubleValue();
/*      */     } catch (Exception localException) {
/*      */     }
/*  870 */     return paramDouble;
/*      */   }
/*      */ 
/*      */   public int optInt(String paramString)
/*      */   {
/*  885 */     return optInt(paramString, 0);
/*      */   }
/*      */ 
/*      */   public int optInt(String paramString, int paramInt)
/*      */   {
/*      */     try
/*      */     {
/*  901 */       return getInt(paramString); } catch (Exception localException) {
/*      */     }
/*  903 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public JSONArray optJSONArray(String paramString)
/*      */   {
/*  917 */     Object localObject = opt(paramString);
/*  918 */     return (localObject instanceof JSONArray) ? (JSONArray)localObject : null;
/*      */   }
/*      */ 
/*      */   public JSONObject optJSONObject(String paramString)
/*      */   {
/*  931 */     Object localObject = opt(paramString);
/*  932 */     return (localObject instanceof JSONObject) ? (JSONObject)localObject : null;
/*      */   }
/*      */ 
/*      */   public long optLong(String paramString)
/*      */   {
/*  946 */     return optLong(paramString, 0L);
/*      */   }
/*      */ 
/*      */   public long optLong(String paramString, long paramLong)
/*      */   {
/*      */     try
/*      */     {
/*  962 */       return getLong(paramString); } catch (Exception localException) {
/*      */     }
/*  964 */     return paramLong;
/*      */   }
/*      */ 
/*      */   public String optString(String paramString)
/*      */   {
/*  978 */     return optString(paramString, "");
/*      */   }
/*      */ 
/*      */   public String optString(String paramString1, String paramString2)
/*      */   {
/*  991 */     Object localObject = opt(paramString1);
/*  992 */     return localObject != null ? localObject.toString() : paramString2;
/*      */   }
/*      */ 
/*      */   public JSONObject put(String paramString, boolean paramBoolean)
/*      */     throws JSONException
/*      */   {
/* 1005 */     put(paramString, paramBoolean ? Boolean.TRUE : Boolean.FALSE);
/* 1006 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject put(String paramString, double paramDouble)
/*      */     throws JSONException
/*      */   {
/* 1019 */     put(paramString, new Double(paramDouble));
/* 1020 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject put(String paramString, int paramInt)
/*      */     throws JSONException
/*      */   {
/* 1033 */     put(paramString, new Integer(paramInt));
/* 1034 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject put(String paramString, long paramLong)
/*      */     throws JSONException
/*      */   {
/* 1047 */     put(paramString, new Long(paramLong));
/* 1048 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject put(String paramString, Map paramMap)
/*      */     throws JSONException
/*      */   {
/* 1061 */     put(paramString, new JSONObject(paramMap));
/* 1062 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject put(String paramString, Object paramObject)
/*      */     throws JSONException
/*      */   {
/* 1078 */     if (paramString == null) {
/* 1079 */       throw new JSONException("Null key.");
/*      */     }
/* 1081 */     if (paramObject != null) {
/* 1082 */       testValidity(paramObject);
/* 1083 */       this.map.put(paramString, paramObject);
/*      */     } else {
/* 1085 */       remove(paramString);
/*      */     }
/* 1087 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject putOnce(String paramString, Object paramObject)
/*      */     throws JSONException
/*      */   {
/* 1101 */     if ((paramString != null) && (paramObject != null)) {
/* 1102 */       if (opt(paramString) != null) {
/* 1103 */         throw new JSONException("Duplicate key \"" + paramString + "\"");
/*      */       }
/* 1105 */       put(paramString, paramObject);
/*      */     }
/* 1107 */     return this;
/*      */   }
/*      */ 
/*      */   public JSONObject putOpt(String paramString, Object paramObject)
/*      */     throws JSONException
/*      */   {
/* 1122 */     if ((paramString != null) && (paramObject != null)) {
/* 1123 */       put(paramString, paramObject);
/*      */     }
/* 1125 */     return this;
/*      */   }
/*      */ 
/*      */   public static String quote(String paramString)
/*      */   {
/* 1138 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 1139 */       return "\"\"";
/*      */     }
/*      */ 
/* 1143 */     int j = 0;
/*      */ 
/* 1145 */     int m = paramString.length();
/* 1146 */     StringBuffer localStringBuffer = new StringBuffer(m + 4);
/*      */ 
/* 1149 */     localStringBuffer.append('"');
/* 1150 */     for (int k = 0; k < m; k++) {
/* 1151 */       int i = j;
/* 1152 */       j = paramString.charAt(k);
/* 1153 */       switch (j) {
/*      */       case 34:
/*      */       case 92:
/* 1156 */         localStringBuffer.append('\\');
/* 1157 */         localStringBuffer.append(j);
/* 1158 */         break;
/*      */       case 47:
/* 1160 */         if (i == 60) {
/* 1161 */           localStringBuffer.append('\\');
/*      */         }
/* 1163 */         localStringBuffer.append(j);
/* 1164 */         break;
/*      */       case 8:
/* 1166 */         localStringBuffer.append("\\b");
/* 1167 */         break;
/*      */       case 9:
/* 1169 */         localStringBuffer.append("\\t");
/* 1170 */         break;
/*      */       case 10:
/* 1172 */         localStringBuffer.append("\\n");
/* 1173 */         break;
/*      */       case 12:
/* 1175 */         localStringBuffer.append("\\f");
/* 1176 */         break;
/*      */       case 13:
/* 1178 */         localStringBuffer.append("\\r");
/* 1179 */         break;
/*      */       default:
/* 1181 */         if ((j < 32) || ((j >= 128) && (j < 160)) || ((j >= 8192) && (j < 8448)))
/*      */         {
/* 1183 */           String str = "000" + Integer.toHexString(j);
/* 1184 */           localStringBuffer.append("\\u" + str.substring(str.length() - 4));
/*      */         } else {
/* 1186 */           localStringBuffer.append(j);
/*      */         }
/*      */       }
/*      */     }
/* 1190 */     localStringBuffer.append('"');
/* 1191 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public Object remove(String paramString)
/*      */   {
/* 1201 */     return this.map.remove(paramString);
/*      */   }
/*      */ 
/*      */   public Iterator sortedKeys()
/*      */   {
/* 1211 */     return new TreeSet(this.map.keySet()).iterator();
/*      */   }
/*      */ 
/*      */   public static Object stringToValue(String paramString)
/*      */   {
/* 1221 */     if (paramString.equals("")) {
/* 1222 */       return paramString;
/*      */     }
/* 1224 */     if (paramString.equalsIgnoreCase("true")) {
/* 1225 */       return Boolean.TRUE;
/*      */     }
/* 1227 */     if (paramString.equalsIgnoreCase("false")) {
/* 1228 */       return Boolean.FALSE;
/*      */     }
/* 1230 */     if (paramString.equalsIgnoreCase("null")) {
/* 1231 */       return NULL;
/*      */     }
/*      */ 
/* 1242 */     int i = paramString.charAt(0);
/* 1243 */     if (((i >= 48) && (i <= 57)) || (i == 46) || (i == 45) || (i == 43)) {
/* 1244 */       if (i == 48)
/* 1245 */         if ((paramString.length() > 2) && ((paramString.charAt(1) == 'x') || (paramString.charAt(1) == 'X')))
/*      */           try
/*      */           {
/* 1248 */             return new Integer(Integer.parseInt(paramString.substring(2), 16));
/*      */           }
/*      */           catch (Exception localException1)
/*      */           {
/*      */           }
/*      */         else
/*      */           try {
/* 1255 */             return new Integer(Integer.parseInt(paramString, 8));
/*      */           }
/*      */           catch (Exception localException2)
/*      */           {
/*      */           }
/*      */       try
/*      */       {
/* 1262 */         if ((paramString.indexOf('.') > -1) || (paramString.indexOf('e') > -1) || (paramString.indexOf('E') > -1)) {
/* 1263 */           return Double.valueOf(paramString);
/*      */         }
/* 1265 */         Long localLong = new Long(paramString);
/* 1266 */         if (localLong.longValue() == localLong.intValue()) {
/* 1267 */           return new Integer(localLong.intValue());
/*      */         }
/* 1269 */         return localLong;
/*      */       }
/*      */       catch (Exception localException3)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1276 */     return paramString;
/*      */   }
/*      */ 
/*      */   static void testValidity(Object paramObject)
/*      */     throws JSONException
/*      */   {
/* 1286 */     if (paramObject != null)
/* 1287 */       if ((paramObject instanceof Double)) {
/* 1288 */         if ((((Double)paramObject).isInfinite()) || (((Double)paramObject).isNaN())) {
/* 1289 */           throw new JSONException("JSON does not allow non-finite numbers.");
/*      */         }
/*      */       }
/* 1292 */       else if (((paramObject instanceof Float)) && (
/* 1293 */         (((Float)paramObject).isInfinite()) || (((Float)paramObject).isNaN())))
/* 1294 */         throw new JSONException("JSON does not allow non-finite numbers.");
/*      */   }
/*      */ 
/*      */   public JSONArray toJSONArray(JSONArray paramJSONArray)
/*      */     throws JSONException
/*      */   {
/* 1311 */     if ((paramJSONArray == null) || (paramJSONArray.length() == 0)) {
/* 1312 */       return null;
/*      */     }
/* 1314 */     JSONArray localJSONArray = new JSONArray();
/* 1315 */     for (int i = 0; i < paramJSONArray.length(); i++) {
/* 1316 */       localJSONArray.put(opt(paramJSONArray.getString(i)));
/*      */     }
/* 1318 */     return localJSONArray;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/* 1335 */       Iterator localIterator = keys();
/* 1336 */       StringBuffer localStringBuffer = new StringBuffer("{");
/*      */ 
/* 1338 */       while (localIterator.hasNext()) {
/* 1339 */         if (localStringBuffer.length() > 1) {
/* 1340 */           localStringBuffer.append(',');
/*      */         }
/* 1342 */         Object localObject = localIterator.next();
/* 1343 */         localStringBuffer.append(quote(localObject.toString()));
/* 1344 */         localStringBuffer.append(':');
/* 1345 */         localStringBuffer.append(valueToString(this.map.get(localObject)));
/*      */       }
/* 1347 */       localStringBuffer.append('}');
/* 1348 */       return localStringBuffer.toString(); } catch (Exception localException) {
/*      */     }
/* 1350 */     return null;
/*      */   }
/*      */ 
/*      */   public String toString(int paramInt)
/*      */     throws JSONException
/*      */   {
/* 1368 */     return toString(paramInt, 0);
/*      */   }
/*      */ 
/*      */   String toString(int paramInt1, int paramInt2)
/*      */     throws JSONException
/*      */   {
/* 1387 */     int j = length();
/* 1388 */     if (j == 0) {
/* 1389 */       return "{}";
/*      */     }
/* 1391 */     Iterator localIterator = sortedKeys();
/* 1392 */     StringBuffer localStringBuffer = new StringBuffer("{");
/* 1393 */     int k = paramInt2 + paramInt1;
/*      */     Object localObject;
/* 1395 */     if (j == 1) {
/* 1396 */       localObject = localIterator.next();
/* 1397 */       localStringBuffer.append(quote(localObject.toString()));
/* 1398 */       localStringBuffer.append(": ");
/* 1399 */       localStringBuffer.append(valueToString(this.map.get(localObject), paramInt1, paramInt2));
/*      */     }
/*      */     else
/*      */     {
/*      */       int i;
/* 1402 */       while (localIterator.hasNext()) {
/* 1403 */         localObject = localIterator.next();
/* 1404 */         if (localStringBuffer.length() > 1)
/* 1405 */           localStringBuffer.append(",\n");
/*      */         else {
/* 1407 */           localStringBuffer.append('\n');
/*      */         }
/* 1409 */         for (i = 0; i < k; i++) {
/* 1410 */           localStringBuffer.append(' ');
/*      */         }
/* 1412 */         localStringBuffer.append(quote(localObject.toString()));
/* 1413 */         localStringBuffer.append(": ");
/* 1414 */         localStringBuffer.append(valueToString(this.map.get(localObject), paramInt1, k));
/*      */       }
/*      */ 
/* 1417 */       if (localStringBuffer.length() > 1) {
/* 1418 */         localStringBuffer.append('\n');
/* 1419 */         for (i = 0; i < paramInt2; i++) {
/* 1420 */           localStringBuffer.append(' ');
/*      */         }
/*      */       }
/*      */     }
/* 1424 */     localStringBuffer.append('}');
/* 1425 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   static String valueToString(Object paramObject)
/*      */     throws JSONException
/*      */   {
/* 1451 */     if ((paramObject == null) || (paramObject.equals(null))) {
/* 1452 */       return "null";
/*      */     }
/* 1454 */     if ((paramObject instanceof JSONString)) {
/*      */       String str;
/*      */       try { str = ((JSONString)paramObject).toJSONString();
/*      */       } catch (Exception localException) {
/* 1459 */         throw new JSONException(localException);
/*      */       }
/* 1461 */       if ((str instanceof String)) {
/* 1462 */         return (String)str;
/*      */       }
/* 1464 */       throw new JSONException("Bad value from toJSONString: " + str);
/*      */     }
/* 1466 */     if ((paramObject instanceof Number)) {
/* 1467 */       return numberToString((Number)paramObject);
/*      */     }
/* 1469 */     if (((paramObject instanceof Boolean)) || ((paramObject instanceof JSONObject)) || ((paramObject instanceof JSONArray)))
/*      */     {
/* 1471 */       return paramObject.toString();
/*      */     }
/* 1473 */     if ((paramObject instanceof Map)) {
/* 1474 */       return new JSONObject((Map)paramObject).toString();
/*      */     }
/* 1476 */     if ((paramObject instanceof Collection)) {
/* 1477 */       return new JSONArray((Collection)paramObject).toString();
/*      */     }
/* 1479 */     if (paramObject.getClass().isArray()) {
/* 1480 */       return new JSONArray(paramObject).toString();
/*      */     }
/* 1482 */     return quote(paramObject.toString());
/*      */   }
/*      */ 
/*      */   static String valueToString(Object paramObject, int paramInt1, int paramInt2)
/*      */     throws JSONException
/*      */   {
/* 1502 */     if ((paramObject == null) || (paramObject.equals(null)))
/* 1503 */       return "null";
/*      */     try
/*      */     {
/* 1506 */       if ((paramObject instanceof JSONString)) {
/* 1507 */         String str = ((JSONString)paramObject).toJSONString();
/* 1508 */         if ((str instanceof String))
/* 1509 */           return (String)str;
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1515 */     if ((paramObject instanceof Number)) {
/* 1516 */       return numberToString((Number)paramObject);
/*      */     }
/* 1518 */     if ((paramObject instanceof Boolean)) {
/* 1519 */       return paramObject.toString();
/*      */     }
/* 1521 */     if ((paramObject instanceof JSONObject)) {
/* 1522 */       return ((JSONObject)paramObject).toString(paramInt1, paramInt2);
/*      */     }
/* 1524 */     if ((paramObject instanceof JSONArray)) {
/* 1525 */       return ((JSONArray)paramObject).toString(paramInt1, paramInt2);
/*      */     }
/* 1527 */     if ((paramObject instanceof Map)) {
/* 1528 */       return new JSONObject((Map)paramObject).toString(paramInt1, paramInt2);
/*      */     }
/* 1530 */     if ((paramObject instanceof Collection)) {
/* 1531 */       return new JSONArray((Collection)paramObject).toString(paramInt1, paramInt2);
/*      */     }
/* 1533 */     if (paramObject.getClass().isArray()) {
/* 1534 */       return new JSONArray(paramObject).toString(paramInt1, paramInt2);
/*      */     }
/* 1536 */     return quote(paramObject.toString());
/*      */   }
/*      */ 
/*      */   public Writer write(Writer paramWriter)
/*      */     throws JSONException
/*      */   {
/*      */     try
/*      */     {
/* 1551 */       int i = 0;
/* 1552 */       Iterator localIterator = keys();
/* 1553 */       paramWriter.write(123);
/*      */ 
/* 1555 */       while (localIterator.hasNext()) {
/* 1556 */         if (i != 0) {
/* 1557 */           paramWriter.write(44);
/*      */         }
/* 1559 */         Object localObject1 = localIterator.next();
/* 1560 */         paramWriter.write(quote(localObject1.toString()));
/* 1561 */         paramWriter.write(58);
/* 1562 */         Object localObject2 = this.map.get(localObject1);
/* 1563 */         if ((localObject2 instanceof JSONObject))
/* 1564 */           ((JSONObject)localObject2).write(paramWriter);
/* 1565 */         else if ((localObject2 instanceof JSONArray))
/* 1566 */           ((JSONArray)localObject2).write(paramWriter);
/*      */         else {
/* 1568 */           paramWriter.write(valueToString(localObject2));
/*      */         }
/* 1570 */         i = 1;
/*      */       }
/* 1572 */       paramWriter.write(125);
/* 1573 */       return paramWriter; } catch (IOException localIOException) {
/*      */     }
String localIOException = null;
/* 1575 */     throw new JSONException(localIOException);
/*      */   }
/*      */ 
/*      */   private static final class Null
/*      */   {
/*      */     protected final Object clone()
/*      */     {
/*  105 */       return this;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  116 */       return (paramObject == null) || (paramObject == this);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  125 */       return "null";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Documents and Settings\hejin\My Documents\Downloads\syno\sFILEAPP.jar
 * Qualified Name:     org.json.JSONObject
 * JD-Core Version:    0.6.0
 */