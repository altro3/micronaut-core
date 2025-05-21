package io.micronaut.http.server.netty.binding

import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.convert.converters.MultiValuesConverterFactory
import io.micronaut.core.convert.format.Format
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.server.netty.AbstractMicronautSpec
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import spock.lang.Unroll

import static io.micronaut.core.convert.converters.MultiValuesConverterFactory.FORMAT_CSV
import static io.micronaut.core.convert.converters.MultiValuesConverterFactory.FORMAT_DEEP_OBJECT
import static io.micronaut.core.convert.converters.MultiValuesConverterFactory.FORMAT_MULTI
import static io.micronaut.core.convert.converters.MultiValuesConverterFactory.FORMAT_PIPES
import static io.micronaut.core.convert.converters.MultiValuesConverterFactory.FORMAT_SSV

class HeaderFormattingSpec extends AbstractMicronautSpec {
    private static String PIPE = "%7C";

    @Unroll
    void "test bind formatted header value for URI #uri and value #value"() {
        given:
        var encodedUri = uri.replace("|", PIPE).replace(" ", "+")
        HttpRequest<?> req = HttpRequest.create(HttpMethod.GET, encodedUri)
        req.header("param", encodedUri)
        Publisher<HttpResponse<?>> exchange = httpClient.exchange(req, String)
        HttpResponse<?> response = Flux.from(exchange).blockFirst()
        def body = response.body()

        expect:
        body == result

        where:
        uri                  | value                                  | result
        // List
        '/formatted/csv'     | 'a,b,c'                                | ["a", "b", "c"].inspect()
        '/formatted/ssv'     | 'a b d'                                | ["a", "b", "d"].inspect()
        '/formatted/pipes'   | 'what|is|life'                         | ["what", "is", "life"].inspect()
        '/formatted/multi'   | 'value=one&value=two&value=3'          | ["one", "two", "3"].inspect()
        '/formatted/deep'    | 'param[0]=a&param[1]=b'                | ["a", "b"].inspect()
        '/formatted/csv'     | ''                                     | [].inspect()
        '/formatted/csv'     | ''                                     | [].inspect()
        '/formatted/pipes'   | '|two'                                 | ['', 'two'].inspect()
        // Map
        '/formatted/m/csv'   | 'key,value,a,b,c,d'                    | ["key": "value", "a": "b", "c": "d"].toSorted().inspect()
        '/formatted/m/ssv'   | 'param=start 0 end 1'                  | ["start": 0, "end": 1].toSorted().inspect()
        '/formatted/m/pipes' | 'a|b|c|d|e'                            | ["a": "b", "c": "d"].toSorted().inspect()
        '/formatted/m/multi' | 'k=1&val=2&c=3'                        | ["k": "1", "val": "2", "c": "3"].toSorted().inspect()
        '/formatted/m/deep'  | 'v[start]=0&v[end]=2&v[middle]=3'      | ["start": 0, "end": 2, "middle": 3].toSorted().inspect()
        '/formatted/m/ssv'   | ''                                     | [:].inspect()
        '/formatted/m/ssv'   | ''                                     | [:].inspect()
        // Object
        '/formatted/o/csv'   | 'name,Doggo,age,12'                    | "name: Doggo, age: 12, weight: null"
        '/formatted/o/ssv'   | 'name Fred weight 2'                   | "name: Fred, age: null, weight: 2.0"
        '/formatted/o/pipes' | 'name|Fred|age|2|weight|3|unknown|1'   | "name: Fred, age: 2, weight: 3.0"
        '/formatted/o/multi' | 'name=Doggo&age=12'                    | "name: Doggo, age: 12, weight: null"
        '/formatted/o/deep'  | 'v[name]=Doggo&v[age]=1&v[weight]=0.5' | "name: Doggo, age: 1, weight: 0.5"
    }

    void "test bind formatted headers object initialization error"() {
        when:
        HttpRequest<?> req = HttpRequest.create(HttpMethod.GET, '/formatted/o/csv')
        Publisher<HttpResponse<?>> exchange = httpClient.exchange(req, String)
        Flux.from(exchange).blockFirst()
        then:
        var e = thrown(HttpClientResponseException)
    }

    @Requires(property = 'spec.name', value = 'HeaderFormattingSpec')
    @Controller(value = "/formatted", produces = MediaType.TEXT_PLAIN)
    static class FormattedController {
        @Get("csv")
        String csvList(@Header @Format(FORMAT_CSV) List<String> param) {
            return param.inspect()
        }

        @Get("ssv")
        String ssvList(@Header("v") @Format(FORMAT_SSV) ArrayList<String> param) {
            return param.inspect()
        }

        @Get("pipes")
        String pipesList(@Header @Format(FORMAT_PIPES) Iterable<String> param) {
            return param.inspect()
        }

        @Get("multi")
        String multiList(@Header("value") @Format(FORMAT_MULTI) Iterable<String> param) {
            return param.inspect()
        }

        @Get("deep")
        String deepList(@Header @Format(FORMAT_DEEP_OBJECT) List<String> param) {
            return param.inspect();
        }

        @Get("m/csv")
        String csvMap(@Header("p") @Format(FORMAT_CSV) Map<String, String> param) {
            return param.toSorted().inspect()
        }

        @Get("m/ssv")
        String ssvMap(@Header @Format(FORMAT_SSV) HashMap<String, Integer> param) {
            return param.toSorted().inspect()
        }

        @Get("m/pipes")
        String pipesMap(@Header @Format(FORMAT_PIPES) Map<CharSequence, CharSequence> param) {
            return param.toSorted().inspect()
        }

        @Get("m/multi")
        String multiMap(@Header("v") @Format(FORMAT_MULTI) Map<String, String> param) {
            return param.toSorted().inspect()
        }

        @Get("m/deep")
        String deepMap(@Header("v") @Format(FORMAT_DEEP_OBJECT) Map<String, Integer> param) {
            return param.toSorted().inspect()
        }

        @Get("o/csv")
        String csvObject(@Header("p") @Format(FORMAT_CSV) Dog param) {
            return param.inspect()
        }

        @Get("o/ssv")
        String ssvObject(@Header @Format(FORMAT_SSV) Dog param) {
            return param.inspect()
        }

        @Get("o/pipes")
        String pipesObject(@Header @Format(FORMAT_PIPES) Dog param) {
            return param.inspect()
        }

        @Get("o/multi")
        String multiObject(@Header("v") @Format(FORMAT_MULTI) Dog param) {
            return param.inspect()
        }

        @Get("o/deep")
        String deepObject(@Header("v") @Format(FORMAT_DEEP_OBJECT) Dog param) {
            return param.inspect()
        }
    }

    @Introspected
    static class Dog {
        private final String name
        private Integer age
        private Float weight

        Dog(String name) {
            this.name = name
        }

        Integer getAge() {
            return age
        }

        void setAge(Integer age) {
            this.age = age
        }

        Float getWeight() {
            return weight
        }

        void setWeight(Float weight) {
            this.weight = weight
        }

        String getName() {
            return name
        }

        @Override
        String toString() {
            return "name: $name, age: $age, weight: $weight"
        }
    }
}
