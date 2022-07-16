package com.tvv.db.util;

/**
 * Fields for database access
 */
public final class Fields {

    public static final String ENTITY__ID = "id";

    //table: Users
    public static final String USER__NUMBER = "number";
    public static final String USER__PASSWORD = "password";
    public static final String USER__FIRST_NAME = "first_name";
    public static final String USER__LAST_NAME = "last_name";
    public static final String USER__DATE_OF_BIRTH = "date_of_birth";
    public static final String USER__PHONE = "phone";
    public static final String USER__STATUS = "status";
    public static final String USER__PHOTO = "photo";
    public static final String USER__ROLE = "role_id";
    public static final String USER__LOCAL = "locale";

    //table: Authors
    public static final String AUTHOR__FIRST_NAME = "first_name";
    public static final String AUTHOR__LAST_NAME = "last_name";

    //table: Books
    public static final String BOOK__ISBN = "isbn";
    public static final String BOOK__NAME = "name";
    public static final String BOOK__YEAR = "year";
    public static final String BOOK__IMAGE = "image";
    public static final String BOOK__PUBLISHER = "publisher_id";

    //table: Genres
    public static final String GENRE__NAME = "name";

    //table: Publishers
    public static final String PUBLISHER__NAME = "name";
    public static final String PUBLISHER__ADDRESS = "address";
    public static final String PUBLISHER__PHONE = "phone";
    public static final String PUBLISHER__CITY = "city";

    //table: Roles
    public static final String ROLE__NAME = "name";
    public static final String ROLE__STATUS = "status";

    //table: Book---Genre
    public static final String BG__BOOK = "book_id";
    public static final String BG__GENRE = "genre_id";

    //table: Book---Publisher
    public static final String BP__BOOK = "book_id";
    public static final String BP__PUBLISHER = "publisher_id";

    //table: Book---Author
    public static final String BA__BOOK = "book_id";
    public static final String BA__AUTHOR = "author_id";


    //table: Book---User
    public static final String BU__BOOK = "book_id";
    public static final String BU__USER = "user_id";
    public static final String BU__START_DATE = "start_date";
    public static final String BU__END_DATE = "end_date";
    public static final String BU__STATUS = "status";
    public static final String BU__STATUS_PAY = "status_pay";

    //table: current books
    public static final String CB__BOOK = "book_id";
    public static final String CB__COUNT = "count";


}