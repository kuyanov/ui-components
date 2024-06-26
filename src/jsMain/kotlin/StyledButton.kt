import csstype.PropertiesBuilder
import emotion.react.css
import react.*
import react.dom.html.ButtonHTMLAttributes
import react.dom.html.ReactHTML.button
import web.cssom.*
import web.html.HTMLButtonElement

external interface StyledButtonAttrs {
    var borderRadius: Double
    var borderWidth: Length
    var colorTheme: ColorTheme
    var cursor: Cursor
    var shadowed: Boolean
}

fun StyledButtonAttrs.defaultStyledButtonAttrs() {
    borderRadius = 0.5
    borderWidth = 0.px
    colorTheme = TransparentButtonTheme()
    cursor = Cursor.default
    shadowed = false
}

external interface StyledButtonPropsGeneric<ButtonBuilderType, ButtonStylesType> :
    StyledButtonAttrs, Props {
    var buttonBuilder: ButtonBuilderType
    var buttonStyles: ButtonStylesType
}

typealias ButtonBuilderType = ButtonHTMLAttributes<HTMLButtonElement>.() -> Unit
typealias ButtonStylesType = PropertiesBuilder.() -> Unit
typealias StyledButtonProps = StyledButtonPropsGeneric<ButtonBuilderType, ButtonStylesType>

fun StyledButtonProps.defaultStyledButtonProps() {
    defaultStyledButtonAttrs()
    buttonBuilder = {}
    buttonStyles = {}
}

fun PropertiesBuilder.styledButtonCSS(attrs: StyledButtonAttrs) {
    backgroundColor = attrs.colorTheme.primaryBackgroundColor
    border = Border(attrs.borderWidth, LineStyle.solid, attrs.colorTheme.primaryBorderColor)
    borderRadius = attrs.borderRadius * 2.5.em
    boxShadow = None.none
    color = attrs.colorTheme.primaryTextColor
    display = Display.inlineBlock
    font = Globals.inherit
    lineHeight = 1.5.em
    margin = 0.em
    outline = None.none
    padding = Padding(0.5.em, 1.em)
    textDecoration = None.none
    hover {
        not("[disabled]") {
            backgroundColor = attrs.colorTheme.hoverBackgroundColor
            borderColor = attrs.colorTheme.hoverBorderColor
            boxShadow = when (attrs.shadowed) {
                true -> BoxShadow(0.px, 2.px, 0.3.em, Color("lightgrey"))
                false -> None.none
            }
            color = attrs.colorTheme.hoverTextColor
            cursor = attrs.cursor
        }
    }
    active {
        not("[disabled]") {
            backgroundColor = attrs.colorTheme.activeBackgroundColor
            borderColor = attrs.colorTheme.activeBorderColor
            boxShadow = when (attrs.shadowed) {
                true -> BoxShadow(0.px, 2.px, 0.3.em, Color("lightgrey"))
                false -> None.none
            }
            color = attrs.colorTheme.activeTextColor
        }
    }
    disabled {
        backgroundColor = attrs.colorTheme.disabledBackgroundColor
        borderColor = attrs.colorTheme.disabledBorderColor
        boxShadow = None.none
        color = attrs.colorTheme.disabledTextColor
    }
}

val StyledButton = FC<StyledButtonProps> { props ->
    button {
        props.buttonBuilder(this)
        css {
            styledButtonCSS(props)
            props.buttonStyles(this)
        }
    }
}

fun ChildrenBuilder.styledButton(propsBuilder: StyledButtonProps.() -> Unit) {
    StyledButton {
        defaultStyledButtonProps()
        propsBuilder()
    }
}
