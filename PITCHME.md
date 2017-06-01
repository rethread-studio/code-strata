## Add Method Invocation

 * Static / non static call
 * Static / non context
 * Internal / external

---

## Non Static External

```java
    @Override
    public synchronized BidiMap<V, K> inverseBidiMap() {
        if (inverse == null) {
            inverse = new UnmodifiableBidiMap<V, K>(decorated().inverseBidiMap());
            inverse.inverse = this;
        }
        return inverse;
    }
```

+++

## Non Static External

```java
@Override
public synchronized BidiMap<V, K> inverseBidiMap() {
    if (inverse == null) {
		try {
			inverse.clear();
		} catch (java.lang.Exception v1043928605631235490057222710587222752877) {}
        inverse = new UnmodifiableBidiMap<V, K>(decorated().inverseBidiMap());
        inverse.inverse = this;
    }
    return inverse;
}

```
@[1-11]
@[4-6]

---

## Static Internal

```java
public Languages.LanguageSet guessLanguages(final String input) {
    final String text = input.toLowerCase(Locale.ENGLISH);

    final Set<String> langs = new HashSet<String>(this.languages.getLanguages());
    for (final LangRule rule : this.rules) {
        if (rule.matches(text)) {
            if (rule.acceptOnMatch) {
                langs.retainAll(rule.languages);
            } else {
                langs.removeAll(rule.languages);
            }
        }
    }

    final Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
    return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
}
```

+++

## Static Internal

```java
public org.apache.commons.codec.language.bm.Lang v1185180809651462963180706011655755821162 = null;

public Languages.LanguageSet guessLanguages(final String input) {
    final String text = input.toLowerCase(Locale.ENGLISH);

    final Set<String> langs = new HashSet<String>(this.languages.getLanguages());
    for (final LangRule rule : this.rules) {
        if (rule.matches(text)) {
            if (rule.acceptOnMatch) {
                langs.retainAll(rule.languages);
            } else {
                langs.removeAll(rule.languages);
            }
        }
    }

    final Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
	try {
	    v1185180809651462963180706011655755821162 = loadFromResource(input, this.languages);
	} catch (java.lang.Exception v405134903526138101558049973510598078831) {}
    return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
}
```
@[1-21]
@[1]
@[18-20]

---

## Swap SubType

 * Target 
```
Interface identifier = new ImplementingType1( (parameter)* );
```
 * replace with 
```
Interface identifier = new ImplementingType2( (parameter)* );
```


---

## Swap SubType

```java
static {
	NAME_PREFIXES.put(NameType.ASHKENAZI,
		Collections.unmodifiableSet(
			new HashSet<String>(Arrays.asList("bar", "ben", "da", "de", "van", "von"))));
	NAME_PREFIXES.put(NameType.SEPHARDIC,
		Collections.unmodifiableSet(
			new HashSet<String>(Arrays.asList("al", "el", "da", "dal", "de", "del", "dela", 
				"de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
	NAME_PREFIXES.put(NameType.GENERIC,
		Collections.unmodifiableSet(
			new HashSet<String>(Arrays.asList("da", "dal", "de", "del", "dela", "de la", "della",
				"des", "di", "do", "dos", "du", "van", "von"))));
}
```

+++

## Swap SubType

```java
static {
	NAME_PREFIXES.put(NameType.ASHKENAZI,
		Collections.unmodifiableSet(
			new java.util.TreeSet<String>(java.util.Arrays.asList("bar", "ben", "da", "de", "van", "von"));
	NAME_PREFIXES.put(NameType.SEPHARDIC,
		Collections.unmodifiableSet(
			new HashSet<String>(Arrays.asList("al", "el", "da", "dal", "de", "del", "dela",
				"de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
	NAME_PREFIXES.put(NameType.GENERIC,
		Collections.unmodifiableSet(
			new HashSet<String>(Arrays.asList("da", "dal", "de", "del", "dela", "de la", "della",
				"des", "di", "do", "dos", "du", "van", "von"))));
}
```
@[1-13]
@[4]

---

## Slide 2

+++

## Slide 2'


