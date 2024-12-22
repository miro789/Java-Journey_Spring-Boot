package vn.miro.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseSuccess extends ResponseEntity<ResponseSuccess.Payload> {

    // PUT, PATCH, DELETE
    public ResponseSuccess(HttpStatusCode status, String message) {
        super(new Payload(status.value(), message), HttpStatus.OK);
    }

    // GET, POST
    public ResponseSuccess(HttpStatusCode status, String message, Object data) {
        super(new Payload(message, data, status.value()), HttpStatus.OK);
    }

    public static class Payload {
        private final int status;
        private final String message;
        private Object data;


        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Payload(String message, Object data, int status) {
            this.message = message;
            this.data = data;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}
