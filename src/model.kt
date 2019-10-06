/**
 * @author benjaminmartinez
 *
 *  High level model for ReleveNumerique and kind of stuff.
 */

/**
 * A compteur d'energie permet de mesurer depuis une [LigneElectrique]. Quelque soit sa déclinaison,
 * il fait toujours celà à l'aide d'une [Pince].
 */
abstract class CompteurEnergie(private val pince: Pince, val ligneElectrique: LigneElectrique) :
    Mesurer<LigneElectrique> {

    override fun getMesure(ligneElectrique: LigneElectrique): ReleveNumerique {
        return ReleveNumerique(ligneElectrique.getMesureConsommation(pince), getFormat())
    }

    abstract fun getFormat() : Format
}

/**
 * Abstraction pour pince servant à une prise de relevé
 */
abstract class Pince

/**
 * Une forme de pince utilisé dans certains compteurs
 */
class PinceAmperometrique : Pince()

/**
 * La consommation electrique d'une ligne, ou autre objet [Mesurable]
 */
class Consommation(val consomated: Long)

/**
 * Tout objet pouvant donner une [Consommation] lorsqu'il est connecté à une [Pince]
 */
interface Mesurable {
    fun getMesureConsommation(prince: Pince): Consommation
}

/**
 * Un exemple d'objet [Mesurable]
 */
abstract class LigneElectrique : Mesurable {
    var consommationLigne: Long = 0
}

/**
 * Objet permettant de traiter un [ReleveNumerique]
 */
interface Concentrateur {
    fun consomme(releve: ReleveNumerique)
}

/**
 * Version synthétique d'une [Consommation]
 * est associé a un format de données
 * Devrait être un wrapper de plusieurs consommations.
 */
class ReleveNumerique(val consommation: Consommation, val format : Format)

/**
 * Objet capable d'effectuer des mesures et renvoyer des [ReleveNumerique]
 */
interface Mesurer<T : Mesurable> {
    fun getMesure(ligneElectrique: T): ReleveNumerique
}

/**
 * Objet qui possede des facultés de transmission à un [Concentrateur]
 */
interface ObjetConnecte {
    fun transmet(concentrateur: Concentrateur)
}

/**
 * Les formats d'émission de [ReleveNumerique]
 */
enum class Format{
    CAYENNE_LPP
}