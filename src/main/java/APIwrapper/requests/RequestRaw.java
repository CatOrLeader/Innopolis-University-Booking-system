package APIwrapper.requests;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

class RequestRaw {
    private final String stringURL;

    public RequestRaw(String url) {
        this.stringURL = url;
    }

    // Main requests to the DB

    protected String getAllBookableRoomsUnformatted() {
        String concreteURL = stringURL + Paths.ROOMS.getPath();

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet(concreteURL)))
        {
            printResponseStatus(response);

            response.addHeader("Content-Type", "application/json");
            response.addHeader("Accept-Language", "en-US");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                return IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            printExceptionMsg(e);
        }

        return null;
    }

    // TODO: Process POST requests correctly
    protected String getFreeRoomsUnformatted(String jsonFreeRoomRequest) {
        String concreteURL = stringURL + Paths.FREE_ROOMS.getPath();

        return sendPOSTReceiveEntity(jsonFreeRoomRequest, concreteURL);
    }

    protected String bookRoomUnformatted(String roomId, String jsonBookRoomRequest) {
        String concreteURL = stringURL + Paths.BOOK_ROOM.getPath(roomId);

        return sendPOSTReceiveEntity(jsonBookRoomRequest, concreteURL);
    }

    protected String queryBookingsUnformatted(String jsonQueryBookingsRequest) {
        String concreteURL = stringURL + Paths.BOOKINGS.getPath();

        return sendPOSTReceiveEntity(jsonQueryBookingsRequest, concreteURL);
    }

    protected String deleteBookingsUnformatted(String bookingsId) {
        String concreteURL = stringURL + Paths.DELETE_BOOKING.getPath(bookingsId);

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpDelete(concreteURL)))
        {
            printResponseStatus(response);

            response.addHeader("Content-Type", "application/json");
            response.addHeader("Accept-Language", "en-US");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                return IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            printExceptionMsg(e);
        }

        return null;
    }

    // Additional processes
    private void printResponseStatus(CloseableHttpResponse response) {
        System.out.println("Protocol version: " + response.getProtocolVersion());
        System.out.println("Status code: " + response.getStatusLine().getStatusCode());
        System.out.println("Reason phrase: " + response.getStatusLine().getReasonPhrase());
        System.out.println("Status line: " + response.getStatusLine().toString());
        System.out.println();
    }

    private void printExceptionMsg(Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }

    private HttpPost createPostRequest(String jsonObj, String concreteUrl) {
        HttpPost post = new HttpPost(concreteUrl);

        StringEntity entity =
                new StringEntity(
                        jsonObj,
                        ContentType.create("application/json", StandardCharsets.UTF_8)
                );

        post.setEntity(entity);

        return post;
    }

    @Nullable
    private String sendPOSTReceiveEntity(String jsonRequestBody, String concreteURL) {
        try (CloseableHttpClient client = HttpClients.createDefault())
        {
            HttpPost post = createPostRequest(jsonRequestBody, concreteURL);

            CloseableHttpResponse response = client.execute(post);
            printResponseStatus(response);

            response.addHeader("Content-Type", "application/json");
            response.addHeader("Accept-Language", "en-US");

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                return IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            printExceptionMsg(e);
        }

        return null;
    }

    public static void main(String[] args) {
        RequestRaw requestRaw =
                new RequestRaw("http://localhost:3000/");

//        requestRaw.getFreeRoomsUnformatted();
    }
}

// Additional Enums for the requests

enum Paths {
    ROOMS {
        @Override
        public String getPath() {
            return "/rooms";
        }
    },
    FREE_ROOMS {
        @Override
        public String getPath() {
            return "/free";
        }
    },
    BOOK_ROOM {
        @Override
        public String getPath(String roomId) {
            return "/rooms/" + roomId + "/book";
        }
    },
    BOOKINGS {
        @Override
        public String getPath() {
            return "/bookings/query";
        }
    },
    DELETE_BOOKING {
        @Override
        public String getPath(String bookingId) {
            return "/bookings/" + bookingId;
        }
    };

    public String getPath() {
        return null;
    }

    public String getPath(String id) {
        return null;
    }
}

