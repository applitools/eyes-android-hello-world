# Eyes Android Espresso Demo application

Android demo application for [Eyes Espresso](https://applitools.com/docs/api-ref/sdk-api/espresso/java/).

## Run the demo

- Clone the repository and open it in Android Studio (Gradle 9.9, AGP 9.5, JDK 17).

- Provide your Applitools API key before building the tests; it is compiled into the test APK. Either export it:

  ```bash
  export APPLITOOLS_API_KEY="your_api_key"
  ```

  or, when building from Android Studio, put `applitoolsApiKey=your_api_key` in `~/.gradle/gradle.properties`.

  You can find your API key under the user menu at the right-hand side of the Test Manager toolbar.
  If you don't yet have an account, [create it now](https://applitools.com/users/register) to obtain your key.

- Run the instrumented tests on an emulator or device (API 26+ for the natural-language test):

  ```bash
  ./gradlew connectedDebugAndroidTest
  ```

## The tests

| Test | What it shows |
| --- | --- |
| `ExampleInstrumentedTest` | The shortest Eyes test: open, two `checkWindow` checkpoints, close. |
| `DocumentationExampleTest` | A runner shared by the suite, region and dialog checkpoints, a Google Map, results per test. |
| `SimpleKotlinTest`, `FullKotlinTest` | The same in Kotlin, with per-test and per-class setup. |
| `NaturalLanguageTest` | Driving the app with `eyes.run("tap 'Click me!'")`: single and multi-step phrases, a `visual check` step, dialogs, scrolling to an element below the fold, and how a failed step reports why. |

Every test reads the key from `BuildConfig.APPLITOOLS_API_KEY`, so nothing in the sources needs editing.

## Natural-language steps

`eyes.run` takes a plain-language instruction between `open` and `close` and performs it on the app:

```java
eyes.open("Hello World", "Tap by label");
eyes.run("tap 'Click me!'");
eyes.run("verify that 'You successfully clicked the button!' is visible");
eyes.check("After tap", Target.window());
eyes.close();
```

A phrase can hold several actions (`"check 'Remember me' and tap 'Sign in'"`); the returned
`AutomationResult` has one entry per step. `eyes.run("visual check")` takes a checkpoint named after the step, the
same as `eyes.check("visual check", Target.window())`. A step that cannot run throws an `AutomationStepException`
whose `getResult()` names the step, the error code and the reason. With `Configuration.setNlpVerbose(true)`
every run logs a report under the `[Applitools NLP]` prefix:

```bash
adb logcat -s ApplitoolsEspresso | grep -A 30 'Applitools NLP'
```

Natural-language steps need Android 8.0 (API 26) or newer and an account with NLP enabled.

### SDK version

The natural-language API is not in a Maven Central release of `eyes-android-espresso` yet. Until it ships,
`gradle.properties` pins `eyesEspressoVersion` and `nmlVersion` to preview builds published to the local Maven
repository from the `eyes.android` monorepo (the SDK preview needs the matching NML preview):

```bash
cd android-nml && ./gradlew publishDebugPublicationToMavenLocal -PcustomVersion=4.0.5-nlp-preview2
```

```bash
cd espresso-sdk && ./gradlew publishDebugPublicationToMavenLocal   # with ext.version set to 5.3.4-nlp-preview2
```

Once released, set `eyesEspressoVersion=latest.release`, remove the `applitools-android-core` line and `nmlVersion`,
and drop `mavenLocal()` from `settings.gradle`.
