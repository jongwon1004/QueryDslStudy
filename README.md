# プロジェクト設定ガイド

このドキュメントは、プロジェクトのQuerydslおよびJUnit設定に関するガイドを提供します。

## Querydsl設定

Querydslを使用するために必要な依存関係を追加します。

```groovy
dependencies {
    // Querydsl
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"

    // Querydsl関連の追加依存関係
    annotationProcessor "jakarta.annotation:jakarta.annotation-api"
    annotationProcessor "jakarta.persistence:jakarta.persistence-api"
}

def querydslSrcDir = 'src/main/generated'
clean {
    delete file(querydslSrcDir)
}
tasks.withType(JavaCompile) {
    options.generatedSourceOutputDirectory = file(querydslSrcDir)
}

## JUnit設定
testImplementation("org.junit.vintage:junit-vintage-engine") {
    exclude group: "org.hamcrest", module: "hamcrest-core"
}
