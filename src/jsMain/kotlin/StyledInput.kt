import csstype.PropertiesBuilder
import emotion.react.css
import react.*
import react.dom.html.InputHTMLAttributes
import react.dom.html.ReactHTML.input
import web.cssom.*
import web.html.HTMLInputElement

enum class StyledInputAppearance {
    Default,
    Underline,
}

external interface StyledInputAttrs {
    var appearance: StyledInputAppearance
    var borderRadius: Length
    var borderWidth: Length
    var colorTheme: ColorTheme
}

fun StyledInputAttrs.defaultStyledInputAttrs() {
    appearance = StyledInputAppearance.Default
    borderRadius = 0.85.em
    borderWidth = 1.px
    colorTheme = TransparentDefaultInputTheme()
}

external interface StyledInputPropsGeneric<InputBuilderType, InputStylesType> :
    StyledInputAttrs, Props {
    var inputBuilder: InputBuilderType
    var inputStyles: InputStylesType
}

typealias InputBuilderType = InputHTMLAttributes<HTMLInputElement>.() -> Unit
typealias InputStylesType = PropertiesBuilder.() -> Unit
typealias StyledInputProps = StyledInputPropsGeneric<InputBuilderType, InputStylesType>

fun StyledInputProps.defaultStyledInputProps() {
    defaultStyledInputAttrs()
    inputBuilder = {}
    inputStyles = {}
}

fun PropertiesBuilder.styledInputCSS(attrs: StyledInputAttrs) {
    backgroundColor = attrs.colorTheme.primaryBackgroundColor
    when (attrs.appearance) {
        StyledInputAppearance.Default -> {
            border = Border(attrs.borderWidth, LineStyle.solid, attrs.colorTheme.primaryBorderColor)
        }

        StyledInputAppearance.Underline -> {
            border = None.none
            borderBottom = Border(attrs.borderWidth, LineStyle.solid, attrs.colorTheme.primaryBorderColor)
        }
    }
    borderRadius = attrs.borderRadius
    boxShadow = None.none
    color = attrs.colorTheme.primaryTextColor
    display = Display.inlineBlock
    font = Globals.inherit
    lineHeight = 1.5.em
    margin = 0.px
    outline = None.none
    padding = Padding(0.1.em, 0.6 * attrs.borderRadius)
    textDecoration = None.none
    hover {
        not("[focus,disabled]") {
            backgroundColor = attrs.colorTheme.hoverBackgroundColor
            borderColor = attrs.colorTheme.hoverBorderColor
            color = attrs.colorTheme.hoverTextColor
        }
    }
    focus {
        backgroundColor = attrs.colorTheme.activeBackgroundColor
        borderColor = attrs.colorTheme.activeBorderColor
        color = attrs.colorTheme.activeTextColor
    }
    disabled {
        backgroundColor = attrs.colorTheme.disabledBackgroundColor
        borderColor = attrs.colorTheme.disabledBorderColor
        color = attrs.colorTheme.disabledTextColor
    }
}

val StyledInput = FC<StyledInputProps> { props ->
    input {
        props.inputBuilder(this)
        css {
            styledInputCSS(props)
            props.inputStyles(this)
        }
    }
}

fun ChildrenBuilder.styledInput(propsBuilder: StyledInputProps.() -> Unit) {
    StyledInput {
        defaultStyledInputProps()
        propsBuilder()
    }
}
