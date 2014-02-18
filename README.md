AndroidCustomTypeface
=====================

Aquí presento mi mejor aproximación para definir una fuente personalizada en los layouts de Android.

Crear una custom View para cada elemento que quieras tener con fuente personalizada TextView, Button, ...

```java
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		parseAttributes(context, attrs);
	}

	private void parseAttributes(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

			String fontName = values.getString(R.styleable.CustomTextView_typeface);

			Typeface tf = CustomTypeface.getInstance(context).getTypeface(fontName);

			setTypeface(tf);
		}
	}
}
```

Añadir en el archivo values/attrs.xml la definición de esta clase para que Android sepa que es una custom view personalizable:
```xml
    <attr name="typeface" format="string" />

    <declare-styleable name="CustomTextView">
        <attr name="typeface" />
    </declare-styleable>
```

Con esto ya podemos añadir la custom view a nuestro layout:
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:custom="http://schemas.android.com/apk/res/com.senechaux.androidcustomtypeface"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <com.senechaux.androidcustomtypeface.CustomTextView
        android:id="@+id/customTextView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/font_infinite"
        custom:typeface="@string/font_infinite" />

</LinearLayout>
```

No hay que olvidarse de añadir el namespace de nuestra aplicación en el elemento raíz: 
```xml
xmlns:custom="http://schemas.android.com/apk/res/com.senechaux.androidcustomtypeface"
```

Y ya podemos indicar al CustomTextView el atributo **custom:typeface**:
```xml
<com.senechaux.androidcustomtypeface.CustomTextView
        custom:typeface="@string/font_infinite" />
```

En este atributo debe ir el nombre de la fuente pero para centralizar los nombres de las fuentes y evitar errores al escribir creamos un archivo de strings en **values/fonts.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <string name="font_bradbunr">BradBunR</string>
    <string name="font_infinite">Infinite</string>
    <string name="font_jfringmaster">JFRingmaster</string>
    <string name="font_jfrocsol">JFRocSol</string>

    <string-array name="fonts_names">
        <item>@string/font_bradbunr</item>
        <item>@string/font_infinite</item>
        <item>@string/font_jfringmaster</item>
        <item>@string/font_jfrocsol</item>
    </string-array>

</resources>
```

Primero definimos los strings con el nombre que queramos para cada fuente en nuestra aplicación en al atributo "name" y como valor del string el nombre del archivo de la fuente sin la extensión pero respetando las mayúsculas del nombre de archivo.
A continuación escribimos un string-array con los nombres que hemos dado nosotros a las fuentes, esto sirve para que la clase encargada de crear los typefaces sepa los nombres de todas las fuentes.

Por último necesitamos una clase Singleton para crear las fuentes una sola vez durante el ciclo de vida de la aplicación ya que en versiones anteriores a Android 4.0 si no se hace esto se crearía el typeface cada vez que se usa la fuente disparando el consumo de memoria.
```java
public class CustomTypeface {
	private static CustomTypeface mInstance = null;
	private Context context;
	private static Hashtable<String, Typeface> typefacesCache;

	private CustomTypeface(Context c) {
		context = c;
		createTypefaces();
	}

	public static CustomTypeface getInstance(Context c) {
		if (mInstance == null) {
			mInstance = new CustomTypeface(c);
		}
		return mInstance;
	}

	private void createTypefaces() {
		typefacesCache = new Hashtable<String, Typeface>();

		String[] fontsNames = context.getResources().getStringArray(R.array.fonts_names);
		for (String fontName : fontsNames) {
			typefacesCache.put(
					fontName,
					Typeface.createFromAsset(context.getAssets(),
							String.format("fonts/%s.ttf", fontName)));
		}

	}

	public Typeface getTypeface(String name) {
		synchronized (typefacesCache) {
			Typeface tf = Typeface.DEFAULT;
			if (null != name && typefacesCache.containsKey(name)) {
				tf = typefacesCache.get(name);
			}
			return tf;
		}
	}

}
```

Los archivos de las fuentes deben estar siempre en la carpeta "assets" y dentro de una subcarpeta llamada "fonts", además la extensión debe ser siempre ".ttf" en minúsculas. Por ejemplo:

>assets/
>>  fonts/
>>>    BradBunR.ttf

>>>    Infinite.ttf

>>>    JFRingmaster.ttf

>>>    JFRocSol.ttf

Y listo! ya podemos definir nuestras fuentes personalizadas en el layout sin ningún tipo de posibilidad de equivocarnos en el nombre de la misma ya que el IDE autocompleta los strings (@string/font_name).
Además para añadir una fuente nueva tan solo hay que añadir el archivo .ttf y añadir su nombre en **values/fonts.xml**.

Cualquier mejora será bienvenida!
