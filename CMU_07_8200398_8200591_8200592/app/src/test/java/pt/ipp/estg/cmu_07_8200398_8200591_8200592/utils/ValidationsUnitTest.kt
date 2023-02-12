package pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.Validation
import org.junit.Test
import org.junit.Assert.*

class ValidationsUnitTest {

    @Test
    fun nameValidations() {
        val namesValidations = listOf(
            Validation("", false),
            Validation("José", true),
            Validation("Nuno", true),
            Validation("Nuno +", false),
            Validation("Luis Sousa", true),
            Validation("Luis Sous@", false),
            Validation("Jorge Correia 1", false),
            Validation("Jorge Correi+a", false),
            Validation("teste@gmail.com", false),
        )

        for(nameValidation in namesValidations){
            if(nameValidation.expecdedValue as Boolean){
                assertTrue(
                    "Value : ${nameValidation.testValue} , Expected Result: true, Obtained result: false",
                    nameValidation(nameValidation.testValue as String)
                )
            }else{
                assertFalse(
                    "Value : ${nameValidation.testValue} , Expected Result: false, Obtained result: true",
                    nameValidation(nameValidation.testValue as String)
                )
            }
        }
    }

    @Test
    fun houseAndDivisionNameVaidations() {
        val houseAndDivisionNamesVaidations = listOf(
            Validation("", false),
            Validation("Casa do José 1", true),
            Validation("Casarão", true),
            Validation("Casa do José@ 1", false),
            Validation("Casa", true),
            Validation("Casa do Nuno +", false),
            Validation("Casa do Luis Sousa", true),
            Validation("Casa do Luis Sous@", false),
            Validation("Casa do Jorge Correia 1", true),
            Validation("Casa do Jorge Correi+a", false),
            Validation("teste@gmail.com", false),
            Validation("Casa do teste@gmail.com", false),
        )

        for(houseAndDivisionNameVaidation in houseAndDivisionNamesVaidations){
            if(houseAndDivisionNameVaidation.expecdedValue as Boolean){
                assertTrue(
                    "Value : ${houseAndDivisionNameVaidation.testValue} , Expected Result: true, Obtained result: false",
                    houseAndDivisionNameVaidation(houseAndDivisionNameVaidation.testValue as String)
                )
            }else{
                assertFalse(
                    "Value : ${houseAndDivisionNameVaidation.testValue} , Expected Result: false, Obtained result: true",
                    houseAndDivisionNameVaidation(houseAndDivisionNameVaidation.testValue as String)
                )
            }
        }
    }

    @Test
    fun emailValidations() {
        val emailsValidations = listOf(
            Validation("teste@gmail.com", true),
            Validation("teste+teste@gmail.com", true),
            Validation("teste@sapo.pt", true),
            Validation("teste@gmailcom", false),
            Validation("teste@gmail.co+m", false),
            Validation("testeagmail.com", false),
            Validation("teste", false),
        )

        for(emailValidation in emailsValidations){
            if(emailValidation.expecdedValue as Boolean){
                assertTrue(
                    "Value : ${emailValidation.testValue} , Expected Result: true, Obtained result: false",
                    emailValidation(emailValidation.testValue as String)
                )
            }else{
                assertFalse(
                    "Value : ${emailValidation.testValue} , Expected Result: false, Obtained result: true",
                    emailValidation(emailValidation.testValue as String)
                )
            }
        }
    }

    @Test
    fun ipValidations() {
        val ipsValidations = listOf(
            Validation("198.168.1.1:10", true),
            Validation("198.168.1.1", true),
            Validation("0.0.0.0:10", true),
            Validation("255.255.255.255:10", true),
            Validation("256.255.255.255:10", false),
            Validation("255.256.255.255:10", false),
            Validation("255.255.256.255:10", false),
            Validation("255.255.255.256:10", false),
            Validation("-255.255.255.255:10", false),
            Validation("255.255.255.255:a", false),
            Validation("255.255.255.2555", false),
            Validation("a.255.255.255:10", false),
            Validation("a.255.255.256", false),
        )

        for(ipValidation in ipsValidations){
            if(ipValidation.expecdedValue as Boolean){
                assertTrue(
                    "Value : ${ipValidation.testValue} , Expected Result: true, Obtained result: false",
                    ipValidation(ipValidation.testValue as String)
                )
            }else{
                assertFalse(
                    "Value : ${ipValidation.testValue} , Expected Result: false, Obtained result: true",
                    ipValidation(ipValidation.testValue as String)
                )
            }
        }
    }

    @Test
    fun passwordValidations(){
        val passwordValidations = listOf(
            Validation("aguitro", false),
            Validation("aguitroi", false),
            Validation("cAtIMPAn", false),
            Validation("T7pe1h2f", false),
            Validation("joaquim", false),
            Validation("1234aA@", false),
            Validation("dXT%3S18", true),
            Validation("12345678aA@", true),
            Validation("12345678aA|-/\\", true),
            Validation("1234aA/\\", true),
        )

        for(passwordValidation in passwordValidations){
            if(passwordValidation.expecdedValue as Boolean){
                assertTrue(
                    "Value : ${passwordValidation.testValue} , Expected Result: true, Obtained result: false",
                    passwordValidation(passwordValidation.testValue as String)
                )
            }else{
                assertFalse(
                    "Value : ${passwordValidation.testValue} , Expected Result: false, Obtained result: true",
                    passwordValidation(passwordValidation.testValue as String)
                )
            }
        }
    }

    @Test
    fun latitudeValidations(){
        val latitudeValidations = listOf(
            Validation(-91.0, false),
            Validation(-90.0, true),
            Validation(-89.0, true),
            Validation(89.0, true),
            Validation(90.0, true),
            Validation(91.0, false),
        )

        for(latitudeValidation in latitudeValidations){
            if(latitudeValidation.expecdedValue as Boolean){
                assertTrue(
                    "Value : ${latitudeValidation.testValue} , Expected Result: true, Obtained result: false",
                    latitudeValidation(latitudeValidation.testValue as Double)
                )
            }else{
                assertFalse(
                    "Value : ${latitudeValidation.testValue} , Expected Result: false, Obtained result: true",
                    latitudeValidation(latitudeValidation.testValue as Double)
                )
            }
        }
    }

    @Test
    fun longitudeValidations(){
        val longitudeValidations = listOf(
            Validation(-181.0, false),
            Validation(-180.0, true),
            Validation(-179.0, true),
            Validation(179.0, true),
            Validation(180.0, true),
            Validation(181.0, false),
        )

        for(longitudeValidation in longitudeValidations){
            if(longitudeValidation.expecdedValue as Boolean){
                assertTrue(
                    "Value : ${longitudeValidation.testValue} , Expected Result: true, Obtained result: false",
                    longitudeValidation(longitudeValidation.testValue as Double)
                )
            }else{
                assertFalse(
                    "Value : ${longitudeValidation.testValue} , Expected Result: false, Obtained result: true",
                    longitudeValidation(longitudeValidation.testValue as Double)
                )
            }
        }
    }
}