import java.util.*

/**
 * @author benjaminmartinez
 *
 *  example du compteur wifi linky2020 un [CompteurEnergie] qui est egalement un [ObjetConnecte]
 */
fun main(args: Array<String>): Unit {
    val ligneElectrique: LigneElectrique = object : LigneElectrique() {
        /**
         * Represente la consommation dans le temps d'une ligne electrique. A chaque fois qu'une consommation
         * est demandée la consommation augmente simulant ainsi la consommation electrique dans le temps d'un foyer.
         */
        override fun getMesureConsommation(prince: Pince): Consommation {
            consommationLigne += 1000
            return Consommation(consommationLigne)
        }
    }
    val concentrateur: Concentrateur = object : Concentrateur {
        override fun consomme(releve: ReleveNumerique) {
            println("Consommation Relevé: ${releve.consommation.consomated}, format: ${releve.format}")
        }
    }

    val linky2020 = CompteurWifi(concentrateur, ligneElectrique)
    linky2020.start()
}

/*
* Compteur wifi wich is a [CompteurEnergie] that is also a [ObjetConnecte]
*/
class CompteurWifi(val concentrateur: Concentrateur, ligneElectrique: LigneElectrique) :
    CompteurEnergie(PinceAmperometrique(), ligneElectrique), ObjetConnecte {

    override fun getFormat(): Format  = Format.CAYENNE_LPP
    /**
     * Appelle le concentrateur toutes les 10 secondes avec la nouvelle mesure de la consommation
     */
    fun start() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                transmet(concentrateur)
            }
        }, 0, 10000)
    }

    /**
     * amelioration possible: isoler le compteur du concentrateur : ici le compteur doit savoir comment envoyer ses données au concentrateur.
     * Or il ne devrait juste que renvoyer un relevé et un autre composant du genre Relais, devrait faire les relevés sur le compteur
     * d'une part, et les envoyer vers le concentrateur d'autre part. Gérant egalement les accès concurrents entre plusieurs compteurs. Ici je sui obligé de construire un objet Concentrateur pour créer un objet Compteur Wifi...
     */
    override fun transmet(concentrateur: Concentrateur) {
        concentrateur.consomme(getMesure(ligneElectrique))
    }
}

