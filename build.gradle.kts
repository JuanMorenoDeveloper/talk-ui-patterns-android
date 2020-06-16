import com.github.salomonbrys.gradle.sass.SassTask
import org.asciidoctor.gradle.jvm.slides.AsciidoctorJRevealJSTask

plugins {
  id("org.asciidoctor.jvm.revealjs") version "3.2.0"
  id("com.github.salomonbrys.gradle.sass") version "1.2.0"
}

repositories {
  jcenter()
  maven {
    url = uri("http://rubygems-proxy.torquebox.org/releases")
  }
}

val sassTask = tasks.named<SassTask>("sassCompile") {
  source = fileTree("src/main/sass")
  outputDir = buildDir.resolve("sass")
}

asciidoctorj {
  modules.diagram.use()
}

val asciidocTask = tasks.named<AsciidoctorJRevealJSTask>("asciidoctorRevealJs") {
  dependsOn(sassTask)
  sourceDir("src/main/asciidoc")
  setOutputDir(buildDir.resolve("../docs"))
  revealjsOptions {
    setCustomThemeLocation(buildDir.resolve("sass/ude.css"))
    setTransition("linear")
    setFragments(true)
    setHighlightJsThemeLocation(file("src/main/sass/highlightjs-theme/github.css"))
    setPushToHistory(true)
    setControls(false)
    setSlideNumber(true)
  }
  clearSecondarySources()
  attributes(mutableMapOf(
      "partials" to file("src/main/asciidoc/partials/"),
      "imagesdir" to "./images",
      "icons" to "font"
  ))
  resources {
    from(sourceDir) {
      include("images/**")
      include("fonts/**")
    }
  }
}

tasks.build {
  dependsOn(asciidocTask)
}
