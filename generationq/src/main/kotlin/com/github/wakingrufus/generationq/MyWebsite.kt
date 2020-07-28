package com.github.wakingrufus.generationq

import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.em
import kotlinx.css.px
import kotlinx.html.*
import java.io.File

object Paths {
    val CSS_PATH = "styles.css"
}

val myFooter: FOOTER.() -> Unit = {
    div {
        style = css {
            display = Display.block
        }
        a(href = mainPage.path) { +"Home" }
    }
}

fun HtmlPage.standardHead() {
    this.head {
        style(type = "text/css") { unsafe { raw(MyStyles().globalStyles()) } }
        link(href = Paths.CSS_PATH, rel = "stylesheet")
        link(href = "http://generationq.libsyn.com/rss", type = "application/rss+xml", rel = "alternate") {
            title = "RSS"
        }
        title(getTitle())
        meta(name = "Description", content = getDescription())
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
    }
}

class MyWebsite {

    fun build(baseDir: File): Website {
        return website(baseDir) {
            cssFile(path = Paths.CSS_PATH, cssString = MyStyles().styles())
            page(mainPage)

        }
    }
}

val mainPage = htmlPage("index.html") {
    standardHead()
    title("Generation Q")
    description("A Star Trek TNG Podcast")
    body {
        pageTitle("Generation Q")
        content {
            div {
                style = css {
                    paddingLeft = 1.em
                }
                p { +"A Star Trek: The Next Generation Podcast" }
                p { a(href = "http://generationq.libsyn.com/rss") { +"RSS" } }
                p {
                    +"Discuss at: "
                    a(href = "https://tenforward.social/@TanagraTooter") {
                        rel = "me"
                        +"tenforward.social"
                    }
                }
            }

            div {
                style = css {
                    paddingLeft = 1.em
                }
                h1 { +"Latest Episode" }
                episode1()
            }

            div {
                style = css {
                    paddingLeft = 1.em
                }
                h1 { +"Episodes" }
                span { a(href = "#s1") { +"Season 1" } }
                episode0()

                div {
                    style = css {
                        paddingTop = 1.em
                        paddingRight = 1.em
                        paddingBottom = 1.em
                        paddingLeft = 1.em
                        borderWidth = 2.px
                        borderColor = Color.silver
                    }
                    a { id = "s1" }
                    h2 { +"Season 1" }
                    episode1()
                }
            }
        }
        footer { myFooter() }
    }
}

val episode0: DIV.() -> Unit = {
    episode("000 - Intro", "https://archive.org/details/GenerationQ-000-Intro",
            "https://tenforward.social/@TanagraTooter/104560793009745258") {
        p { +"The hosts John and Andy introduce themselves, and what their goal is with this podcast." }
    }
}

val episode1: DIV.() -> Unit = {
    episode("001 - S1E01 - Encounter at Farpoint",
            "https://archive.org/details/001-s1e01-encounter-at-farpoint",
            "https://tenforward.social/@TanagraTooter/104593651455494052") {
        p {
            +"""We look at the pilot episode "Encounter at Farpoint", 
                |a 2-part episode that introduces us to the Next Generation crew and establishes some of the main themes of this series.
            """.trimMargin()
        }
        p { +"This episode is longer due to covering both parts of the episode (technically episodes 1&2)" }
    }
}

fun DIV.episode(title: String, url: String, discussionUrl: String, description: DIV.() -> Unit) {
    div {
        style = css {
            backgroundColor = MyStyles.LIGHT_BACKGROUND_COLOR
            paddingBottom = 1.em
            borderColor = Color.white
            borderWidth = 2.px

        }
        h2 { +title }
        description()
        p {
            a(href = url) { +"MP3" }
        }
        p {
            a(href = discussionUrl) { +"Discuss this episode" }
        }
        p {
            span {
                a(href = "https://www.deviantart.com/benttibisson/art/Star-Trek-the-Next-Generation-painting-617241159") {
                    +"Artwork: Bentti Bisson"
                }
            }
            span {
                +" - "
            }
            span {
                a(href = "https://soundcloud.com/nes-house") {
                    +"Intro Music: NES House - Star Trek the next generation House"
                }
            }
            span {
                +" - "
            }
            span {
                a(href = "https://soundcloud.com/aeop") {
                    +"Outro Music: Aeop - A308 - What Do You Want Will Riker"
                }
            }
        }
    }
}
