PGDMP       0                }            KK Enterprises    17.2    17.2 P    ]           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            ^           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            _           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            `           1262    16458    KK Enterprises    DATABASE     �   CREATE DATABASE "KK Enterprises" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_India.1252';
     DROP DATABASE "KK Enterprises";
                     postgres    false            a           0    0    DATABASE "KK Enterprises"    COMMENT     �   COMMENT ON DATABASE "KK Enterprises" IS 'This database use in the java project to develop the application for KK Enterprise...';
                        postgres    false    4960            �            1259    16712    attendance_id_seq    SEQUENCE     z   CREATE SEQUENCE public.attendance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.attendance_id_seq;
       public               postgres    false            �            1259    16532 
   attendance    TABLE     �   CREATE TABLE public.attendance (
    id bigint DEFAULT nextval('public.attendance_id_seq'::regclass) NOT NULL,
    status text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    attendance_date date
);
    DROP TABLE public.attendance;
       public         heap r       postgres    false    231            �            1259    16662    employees_id    SEQUENCE     u   CREATE SEQUENCE public.employees_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.employees_id;
       public               postgres    false            �            1259    16473 	   employees    TABLE     v  CREATE TABLE public.employees (
    id bigint DEFAULT nextval('public.employees_id'::regclass) NOT NULL,
    name text NOT NULL,
    address text NOT NULL,
    mobile_no text NOT NULL,
    email text,
    gender text NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    user_id bigint,
    working_status boolean
);
    DROP TABLE public.employees;
       public         heap r       postgres    false    228            �            1259    16586 
   guage_rate    TABLE     �   CREATE TABLE public.guage_rate (
    id integer NOT NULL,
    rate double precision,
    guage double precision,
    user_id bigint,
    size_id bigint
);
    DROP TABLE public.guage_rate;
       public         heap r       postgres    false            �            1259    16585    guage_rate_id_seq    SEQUENCE     �   CREATE SEQUENCE public.guage_rate_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.guage_rate_id_seq;
       public               postgres    false    225            b           0    0    guage_rate_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.guage_rate_id_seq OWNED BY public.guage_rate.id;
          public               postgres    false    224            �            1259    16870    late_arrivals    TABLE     {  CREATE TABLE public.late_arrivals (
    id bigint NOT NULL,
    emp_id integer,
    no_of_hours double precision,
    date date,
    "time" text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id bigint,
    description text DEFAULT '-'::text,
    emp_wise_entry_no integer
);
 !   DROP TABLE public.late_arrivals;
       public         heap r       postgres    false            �            1259    16869    late_arrivals_id_seq    SEQUENCE     �   CREATE SEQUENCE public.late_arrivals_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.late_arrivals_id_seq;
       public               postgres    false    237            c           0    0    late_arrivals_id_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.late_arrivals_id_seq OWNED BY public.late_arrivals.id;
          public               postgres    false    236            �            1259    16758    prepayments    TABLE     �   CREATE TABLE public.prepayments (
    id integer NOT NULL,
    emp_id integer,
    amount integer,
    date date,
    emp_wise_entry_id integer,
    description text DEFAULT '-'::text
);
    DROP TABLE public.prepayments;
       public         heap r       postgres    false            �            1259    16757    prepayments_id_seq    SEQUENCE     �   CREATE SEQUENCE public.prepayments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.prepayments_id_seq;
       public               postgres    false    233            d           0    0    prepayments_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.prepayments_id_seq OWNED BY public.prepayments.id;
          public               postgres    false    232            �            1259    16779    salaries_id_seq    SEQUENCE     x   CREATE SEQUENCE public.salaries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.salaries_id_seq;
       public               postgres    false            �            1259    16489    salaries    TABLE     �  CREATE TABLE public.salaries (
    id bigint DEFAULT nextval('public.salaries_id_seq'::regclass) NOT NULL,
    emp_id bigint NOT NULL,
    basic_salary numeric NOT NULL,
    advance_amt numeric,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    payment_month date,
    deposit_amount integer,
    overtime_hours double precision,
    rate_of_per_hour integer
);
    DROP TABLE public.salaries;
       public         heap r       postgres    false    234            �            1259    16638    sizes    TABLE     k   CREATE TABLE public.sizes (
    id integer NOT NULL,
    size character varying(10),
    user_id bigint
);
    DROP TABLE public.sizes;
       public         heap r       postgres    false            �            1259    16637    sizes_id_seq    SEQUENCE     �   CREATE SEQUENCE public.sizes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.sizes_id_seq;
       public               postgres    false    227            e           0    0    sizes_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.sizes_id_seq OWNED BY public.sizes.id;
          public               postgres    false    226            �            1259    16549    stocks    TABLE     o  CREATE TABLE public.stocks (
    id bigint NOT NULL,
    day_wise_entry_no integer,
    size text,
    size_type text,
    rate integer,
    bag integer,
    weight double precision,
    guage integer,
    is_small text DEFAULT false,
    entry_month date,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    user_id bigint
);
    DROP TABLE public.stocks;
       public         heap r       postgres    false            �            1259    16548    stocks_id_seq    SEQUENCE     �   CREATE SEQUENCE public.stocks_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.stocks_id_seq;
       public               postgres    false    223            f           0    0    stocks_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.stocks_id_seq OWNED BY public.stocks.id;
          public               postgres    false    222            �            1259    16710    user_id_sequence    SEQUENCE     y   CREATE SEQUENCE public.user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.user_id_sequence;
       public               postgres    false            �            1259    16459    users    TABLE       CREATE TABLE public.users (
    id bigint NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    email text,
    phone_no text NOT NULL,
    join_date date NOT NULL,
    address text NOT NULL,
    "isVerify" boolean DEFAULT false NOT NULL
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    16711    users_id_seq    SEQUENCE     u   CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public               postgres    false            �            1259    16781    vouchers_id_seq    SEQUENCE     x   CREATE SEQUENCE public.vouchers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.vouchers_id_seq;
       public               postgres    false            �            1259    16504    vouchers    TABLE     �  CREATE TABLE public.vouchers (
    id bigint DEFAULT nextval('public.vouchers_id_seq'::regclass) NOT NULL,
    status text NOT NULL,
    payment_state text NOT NULL,
    description text NOT NULL,
    amount numeric NOT NULL,
    user_id bigint NOT NULL,
    emp_id bigint,
    date date,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    receipt_voucher_no integer,
    salaries_id bigint
);
    DROP TABLE public.vouchers;
       public         heap r       postgres    false    235            �           2604    16589    guage_rate id    DEFAULT     n   ALTER TABLE ONLY public.guage_rate ALTER COLUMN id SET DEFAULT nextval('public.guage_rate_id_seq'::regclass);
 <   ALTER TABLE public.guage_rate ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    225    224    225            �           2604    16878    late_arrivals id    DEFAULT     t   ALTER TABLE ONLY public.late_arrivals ALTER COLUMN id SET DEFAULT nextval('public.late_arrivals_id_seq'::regclass);
 ?   ALTER TABLE public.late_arrivals ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    236    237    237            �           2604    16761    prepayments id    DEFAULT     p   ALTER TABLE ONLY public.prepayments ALTER COLUMN id SET DEFAULT nextval('public.prepayments_id_seq'::regclass);
 =   ALTER TABLE public.prepayments ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    233    232    233            �           2604    16641    sizes id    DEFAULT     d   ALTER TABLE ONLY public.sizes ALTER COLUMN id SET DEFAULT nextval('public.sizes_id_seq'::regclass);
 7   ALTER TABLE public.sizes ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    226    227    227            �           2604    16557 	   stocks id    DEFAULT     f   ALTER TABLE ONLY public.stocks ALTER COLUMN id SET DEFAULT nextval('public.stocks_id_seq'::regclass);
 8   ALTER TABLE public.stocks ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    223    222    223            J          0    16532 
   attendance 
   TABLE DATA           Y   COPY public.attendance (id, status, created_at, updated_at, attendance_date) FROM stdin;
    public               postgres    false    221   �`       G          0    16473 	   employees 
   TABLE DATA           �   COPY public.employees (id, name, address, mobile_no, email, gender, created_at, updated_at, user_id, working_status) FROM stdin;
    public               postgres    false    218   	a       N          0    16586 
   guage_rate 
   TABLE DATA           G   COPY public.guage_rate (id, rate, guage, user_id, size_id) FROM stdin;
    public               postgres    false    225   &a       Z          0    16870    late_arrivals 
   TABLE DATA           �   COPY public.late_arrivals (id, emp_id, no_of_hours, date, "time", created_at, updated_at, user_id, description, emp_wise_entry_no) FROM stdin;
    public               postgres    false    237   Ca       V          0    16758    prepayments 
   TABLE DATA           _   COPY public.prepayments (id, emp_id, amount, date, emp_wise_entry_id, description) FROM stdin;
    public               postgres    false    233   `a       H          0    16489    salaries 
   TABLE DATA           �   COPY public.salaries (id, emp_id, basic_salary, advance_amt, created_at, updated_at, payment_month, deposit_amount, overtime_hours, rate_of_per_hour) FROM stdin;
    public               postgres    false    219   }a       P          0    16638    sizes 
   TABLE DATA           2   COPY public.sizes (id, size, user_id) FROM stdin;
    public               postgres    false    227   �a       L          0    16549    stocks 
   TABLE DATA           �   COPY public.stocks (id, day_wise_entry_no, size, size_type, rate, bag, weight, guage, is_small, entry_month, created_at, updated_at, user_id) FROM stdin;
    public               postgres    false    223   �a       F          0    16459    users 
   TABLE DATA           h   COPY public.users (id, username, password, email, phone_no, join_date, address, "isVerify") FROM stdin;
    public               postgres    false    217   �a       I          0    16504    vouchers 
   TABLE DATA           �   COPY public.vouchers (id, status, payment_state, description, amount, user_id, emp_id, date, created_at, updated_at, receipt_voucher_no, salaries_id) FROM stdin;
    public               postgres    false    220   2b       g           0    0    attendance_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.attendance_id_seq', 33, true);
          public               postgres    false    231            h           0    0    employees_id    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.employees_id', 30, true);
          public               postgres    false    228            i           0    0    guage_rate_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.guage_rate_id_seq', 1, false);
          public               postgres    false    224            j           0    0    late_arrivals_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.late_arrivals_id_seq', 1, false);
          public               postgres    false    236            k           0    0    prepayments_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.prepayments_id_seq', 1, false);
          public               postgres    false    232            l           0    0    salaries_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.salaries_id_seq', 68, true);
          public               postgres    false    234            m           0    0    sizes_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.sizes_id_seq', 1, false);
          public               postgres    false    226            n           0    0    stocks_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.stocks_id_seq', 1, false);
          public               postgres    false    222            o           0    0    user_id_sequence    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.user_id_sequence', 1, false);
          public               postgres    false    229            p           0    0    users_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.users_id_seq', 1, false);
          public               postgres    false    230            q           0    0    vouchers_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.vouchers_id_seq', 189, true);
          public               postgres    false    235            �           2606    16538    attendance Attendance_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT "Attendance_pkey" PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.attendance DROP CONSTRAINT "Attendance_pkey";
       public                 postgres    false    221            �           2606    16479    employees employees_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.employees
    ADD CONSTRAINT employees_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.employees DROP CONSTRAINT employees_pkey;
       public                 postgres    false    218            �           2606    16591    guage_rate guage_rate_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.guage_rate
    ADD CONSTRAINT guage_rate_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.guage_rate DROP CONSTRAINT guage_rate_pkey;
       public                 postgres    false    225            �           2606    16880     late_arrivals late_arrivals_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.late_arrivals
    ADD CONSTRAINT late_arrivals_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.late_arrivals DROP CONSTRAINT late_arrivals_pkey;
       public                 postgres    false    237            �           2606    16510    vouchers payments_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.vouchers
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.vouchers DROP CONSTRAINT payments_pkey;
       public                 postgres    false    220            �           2606    16763    prepayments prepayments_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.prepayments
    ADD CONSTRAINT prepayments_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.prepayments DROP CONSTRAINT prepayments_pkey;
       public                 postgres    false    233            �           2606    16495    salaries salaries_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.salaries
    ADD CONSTRAINT salaries_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.salaries DROP CONSTRAINT salaries_pkey;
       public                 postgres    false    219            �           2606    16643    sizes sizes_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.sizes
    ADD CONSTRAINT sizes_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.sizes DROP CONSTRAINT sizes_pkey;
       public                 postgres    false    227            �           2606    16559    stocks stocks_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.stocks
    ADD CONSTRAINT stocks_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.stocks DROP CONSTRAINT stocks_pkey;
       public                 postgres    false    223            �           2606    16466    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    217            �           1259    16526    user_id    INDEX     @   CREATE INDEX user_id ON public.employees USING btree (user_id);
    DROP INDEX public.user_id;
       public                 postgres    false    218            �           2606    16516    vouchers emp_id    FK CONSTRAINT     q   ALTER TABLE ONLY public.vouchers
    ADD CONSTRAINT emp_id FOREIGN KEY (emp_id) REFERENCES public.employees(id);
 9   ALTER TABLE ONLY public.vouchers DROP CONSTRAINT emp_id;
       public               postgres    false    220    218    4759            �           2606    16885    late_arrivals emp_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.late_arrivals
    ADD CONSTRAINT emp_id FOREIGN KEY (emp_id) REFERENCES public.employees(id) NOT VALID;
 >   ALTER TABLE ONLY public.late_arrivals DROP CONSTRAINT emp_id;
       public               postgres    false    218    4759    237            �           2606    16890 (   late_arrivals late_arrivals_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.late_arrivals
    ADD CONSTRAINT late_arrivals_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;
 R   ALTER TABLE ONLY public.late_arrivals DROP CONSTRAINT late_arrivals_user_id_fkey;
       public               postgres    false    4757    237    217            �           2606    16764 #   prepayments prepayments_emp_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prepayments
    ADD CONSTRAINT prepayments_emp_id_fkey FOREIGN KEY (emp_id) REFERENCES public.employees(id) NOT VALID;
 M   ALTER TABLE ONLY public.prepayments DROP CONSTRAINT prepayments_emp_id_fkey;
       public               postgres    false    218    233    4759            �           2606    16496    salaries salaries_emp_id_fkey    FK CONSTRAINT        ALTER TABLE ONLY public.salaries
    ADD CONSTRAINT salaries_emp_id_fkey FOREIGN KEY (emp_id) REFERENCES public.employees(id);
 G   ALTER TABLE ONLY public.salaries DROP CONSTRAINT salaries_emp_id_fkey;
       public               postgres    false    219    4759    218            �           2606    16644    guage_rate size_id    FK CONSTRAINT     {   ALTER TABLE ONLY public.guage_rate
    ADD CONSTRAINT size_id FOREIGN KEY (size_id) REFERENCES public.sizes(id) NOT VALID;
 <   ALTER TABLE ONLY public.guage_rate DROP CONSTRAINT size_id;
       public               postgres    false    225    4772    227            �           2606    16511    vouchers user_id    FK CONSTRAINT     o   ALTER TABLE ONLY public.vouchers
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id);
 :   ALTER TABLE ONLY public.vouchers DROP CONSTRAINT user_id;
       public               postgres    false    220    4757    217            �           2606    16521    employees user_id    FK CONSTRAINT     z   ALTER TABLE ONLY public.employees
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;
 ;   ALTER TABLE ONLY public.employees DROP CONSTRAINT user_id;
       public               postgres    false    218    217    4757            �           2606    16573    stocks user_id    FK CONSTRAINT     w   ALTER TABLE ONLY public.stocks
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;
 8   ALTER TABLE ONLY public.stocks DROP CONSTRAINT user_id;
       public               postgres    false    4757    217    223            �           2606    16632    guage_rate user_id    FK CONSTRAINT     {   ALTER TABLE ONLY public.guage_rate
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;
 <   ALTER TABLE ONLY public.guage_rate DROP CONSTRAINT user_id;
       public               postgres    false    4757    217    225            �           2606    16649    sizes user_id    FK CONSTRAINT     v   ALTER TABLE ONLY public.sizes
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;
 7   ALTER TABLE ONLY public.sizes DROP CONSTRAINT user_id;
       public               postgres    false    217    227    4757            �           2606    16961 "   vouchers vouchers_salaries_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.vouchers
    ADD CONSTRAINT vouchers_salaries_id_fkey FOREIGN KEY (salaries_id) REFERENCES public.salaries(id) NOT VALID;
 L   ALTER TABLE ONLY public.vouchers DROP CONSTRAINT vouchers_salaries_id_fkey;
       public               postgres    false    4762    220    219            J      x������ � �      G      x������ � �      N      x������ � �      Z      x������ � �      V      x������ � �      H      x������ � �      P      x������ � �      L      x������ � �      F   N   x�340�LL����4426153��p�s3s���s9-,��LM���8��Lu�t�9�3�2�sJ�8K�b���� ���      I      x������ � �     