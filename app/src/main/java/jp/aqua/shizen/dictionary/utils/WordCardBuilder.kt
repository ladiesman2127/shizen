package jp.aqua.shizen.dictionary.utils

class WordCard {
    class Builder() {

        private var front: String = "\n"
        private var back: String = "\n"
        fun setFront(front: String) = apply {
            this.front = front.replace(
                "(\\r?\\n|\\r)".toRegex(),
                HtmlConstants.NEW_LINE
            )
        }

        fun setBack(back: String) = apply {
            this.back = back.replace(
                "(\\r?\\n|\\r)".toRegex(),
                HtmlConstants.NEW_LINE
            )
        }

        fun build(): String {
            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title></title>\n" +
                    "</head>\n" +
                    "<style>\n" +
                    "    * {\n" +
                    "        padding: 0;\n" +
                    "        margin: 0;\n" +
                    "        max-width: 100vw;\n" +
                    "        max-height: 100vh;\n" +
                    "    }\n" +
                    "\n" +
                    "    .card {\n" +
                    "        position: absolute;\n" +
                    "        background-color: transparent;\n" +
                    "        width: 100vw;\n" +
                    "        height: 100vh;\n" +
                    "        perspective: 2000px;\n" +
                    "    }\n" +
                    "\n" +
                    "    .card-content {\n" +
                    "        display: flex;\n" +
                    "        justify-content: center;\n" +
                    "        align-items: center;\n" +
                    "        background-color: transparent;\n" +
                    "        width: 100%;\n" +
                    "        height: 100%;\n" +
                    "        transition: transform 1.5s;\n" +
                    "        transform-style: preserve-3d;\n" +
                    "    }\n" +
                    "\n" +
                    "    .card-content.rotate {\n" +
                    "        transform: rotateY(180deg);\n" +
                    "    }\n" +
                    "\n" +
                    "    .card-front {\n" +
                    "        -webkit-backface-visibility: hidden;\n" +
                    "        backface-visibility: hidden;\n" +
                    "    }\n" +
                    "\n" +
                    "    .card-back {\n" +
                    "        -webkit-backface-visibility: hidden;\n" +
                    "        backface-visibility: hidden;\n" +
                    "        transform: rotateY(180deg);\n" +
                    "    }\n" +
                    "</style>\n" +
                    "\n" +
                    "<body>\n" +
                    "    <div class=\"card\">\n" +
                    "        <div class=\"card-content\">\n" +
                    "            <div class=\"card-front\">\n" +
                    "                $front\n" +
                    "            </div>\n" +
                    "            <div class=\"card-back\">\n" +
                    "                $back\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "    <script>\n" +
                    "        var card = document.querySelector(\".card-content\")\n" +
                    "        card.addEventListener('click', () => {\n" +
                    "            card.classList.toggle('rotate')\n" +
                    "        })\n" +
                    "    </script>\n" +
                    "</body>\n" +
                    "</html>"
        }
    }
}
