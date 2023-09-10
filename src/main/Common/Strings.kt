class Strings {
    companion object {
        object Warnings {

        }

        object Errors {
            const val incorrectPlayerIndex = "Недопустимый индекс игрока"
            const val cordsOutOfBounds = "Координаты за пределами доски"
            const val noCheckerInCell = "На данной клетке нет шашки"
            const val colorPlayerCheckerMismatch = "Цвет шашки не соответствует цвету игрока"
            const val incorrectMove = "Недопустимый ход. Попробуйте снова"
            const val notBeatCheckerSelected = "Сейчас можно ходить только шашками с доступной атакой"
        }
    }
}
