package kr.co.mcedu.riot;

public enum RiotApiResponseCode {
    DEFAULT("000"),
    SUCCESS("200"),
    TIMEOUT("998"),
    PROPERTY_ERROR("999");


    RiotApiResponseCode(String state) {

    }

    //TODO : 좀더 생각해볼것
//    companion object {
//        private val states = values().toList().stream().collect(Collectors.toMap(RiotApiResponseCode::state, Function.identity()))
//        fun findByState(state: String): RiotApiResponseCode {
//            return states.getOrDefault(state, DEFAULT)
//        }
//    }
}
