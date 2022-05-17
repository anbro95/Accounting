package sample;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

// Клас для підключення до серверу, збереження SQL-запитів та функцій, пов'язаних з ними
public class dbhandler {
    Connection dbConnection;
// Підключення до серверу
    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "electrical_service";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, "root", "root");
        return dbConnection;
    }
// Метод реєстрації нового користувача системи
    public void signupuser(User user) {
        String insert = "INSERT INTO users(name, surname, login, users_password, isadmin) " +
                "VALUES (?,?,?,?,?)";
        try {
            PreparedStatement prst = getDbConnection().prepareStatement(insert);
            prst.setString(1, user.getName());
            prst.setString(2, user.getSurname());
            prst.setString(3, user.getLogin());
            prst.setString(4, user.getPassword());
            prst.setString(5, user.getIsadmin());


            prst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
// Метод перевірки логіну та паролю користувача для авторизації
    public ResultSet getUser(User user) {
        ResultSet resset = null;

        String select = "SELECT * FROM users WHERE login = ? AND users_password = ?";

        try {
            PreparedStatement prst = getDbConnection().prepareStatement(select);
            prst.setString(1, user.getLogin());
            prst.setString(2, user.getPassword());


            resset = prst.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resset;
    }
//Метод для перевірки чи має користувач права адміна
    public boolean isadmin(User user) throws SQLException, ClassNotFoundException {
        ResultSet resset = null;
        String select = "SELECT users.isadmin FROM users WHERE users.login = ?";
        PreparedStatement p = getDbConnection().prepareStatement(select);
        p.setString(1, user.getLogin());
        resset = p.executeQuery();
        String admin = "Ні";

        while(resset.next()) {
            admin = resset.getString(1);
        }

        System.out.println(admin);



        if(admin.equals("Так")) {
            System.out.println("It is an admin");
            return true;
        } else if(admin == "Null"){
            System.out.println("It is not admin");
            return false;
        } else {
            return false;
        }

    }
// Метод для знаходження абоненту за прізвищем
    public String findabonent(String surname) {

        ResultSet resset = null;
        String query = "SELECT * FROM abonent WHERE abonent.surname = " + "'" + surname + "'" + ";";
        String result = "nothing";

        PreparedStatement p = null;
        try {
            p = getDbConnection().prepareStatement(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            resset = p.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String id = "Нема інформаціїa";
        String name = "";
        String sur = "Нема інформації";
        String third = "";
        String energy = "Нема інформації";
        while (true) {
            try {
                if (!resset.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                id = resset.getString(1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                name = resset.getString(2);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                sur = resset.getString(3);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                third = resset.getString(4);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                energy = resset.getString(5);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        String resstr = "ПІБ: " + sur + " " + name + " " + third + "\n" +
                "Витрачена енергія: " + energy;

        return resstr;
    }
// Метод для додання нового абоненту
    public void addabonent(String name, String surname, String third) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO abonent(name, surname, third_name) VALUES (?,?,?)";

        PreparedStatement p = getDbConnection().prepareStatement(insert);

        p.setString(1, name);
        p.setString(2, surname);
        p.setString(3, third);

        p.executeUpdate();

    }
// Метод для видалення абоненту з системи
    public void deleteabonent(String surname) throws SQLException, ClassNotFoundException {
        String delete = "DELETE FROM abonent WHERE abonent.surname = " + "'" + surname + "'";

        PreparedStatement p = getDbConnection().prepareStatement(delete);

        p.executeUpdate();
    }
// Метод для знаходження контракту за прізвищем абонента
    public String findcontract(String surname) throws SQLException, ClassNotFoundException {
        String find = "SELECT * from contract \n" +
                "JOIN abonent ON contract.abonent = abonent.id\n" +
                "WHERE abonent.surname = " + "'" + surname + "'";
        ResultSet r = null;

        PreparedStatement p = getDbConnection().prepareStatement(find);
        r = p.executeQuery();

        String adress = "Нема інформації";
        String tarif = "Нема інформації";
        String price = "Нема інформації";


        while (r.next()) {
            adress = r.getString(3);
            tarif = r.getString(4);
            price = r.getString(5);
        }

        String result = "Адреса: " + adress + "\n" +
                "Тариф: " + tarif + "\n" +
                "Ціна: " + price;

        return result;
    }
// Метод для укладення нового контракту
    public void addcontract(String abonent, String adress, String tarif, String price) throws SQLException, ClassNotFoundException {
        ResultSet r = null;

        String findid = "SELECT abonent.id FROM abonent \n" +
                "WHERE abonent.surname = " + "'" + abonent + "'";
        PreparedStatement id = getDbConnection().prepareStatement(findid);
        r = id.executeQuery();

        String idresult = "052";
        while (r.next()) {
            idresult = r.getString(1);
        }

        String add = "INSERT INTO contract(abonent, adress, tarif, price) VALUES(?,?,?,?)";

        PreparedStatement p = getDbConnection().prepareStatement(add);

        p.setString(1, idresult);
        p.setString(2, adress);
        p.setString(3, tarif);
        p.setString(4, price);

        p.executeUpdate();
    }
// Метод для видалення контракту
    public void deletecontract(String surname) throws SQLException, ClassNotFoundException {
        String delete = "DELETE FROM contract\n" +
                "WHERE contract.abonent IN (\n" +
                "\tSELECT abonent.id FROM abonent \n" +
                "    WHERE abonent.surname = " + "'" + surname + "'" +
                ")";

        PreparedStatement p = getDbConnection().prepareStatement(delete);

        p.executeUpdate();
    }
// Метод для додання нової аварії
    public void addcrash(String type, String adress) throws SQLException, ClassNotFoundException {
        String add = "INSERT INTO crash(type, adress) VALUES(?,?)";

        PreparedStatement p = getDbConnection().prepareStatement(add);

        p.setString(1, type);
        p.setString(2, adress);

        p.executeUpdate();
    }
// Метод для додання нової роботи
    public void addwork(String type, String date) throws SQLException, ClassNotFoundException {
        String add = "INSERT INTO robota(type, robota_date) VALUES(?,?)";

        PreparedStatement p = getDbConnection().prepareStatement(add);

        p.setString(1, type);
        p.setString(2, date);

        p.executeUpdate();
    }
// Метод для визначення бригади за адресою аварії, з якою вона працювала
    public String findbrigadebyadress(String adress) throws SQLException, ClassNotFoundException {
        ResultSet resset = null;

        String find = "SELECT brigade.id FROM brigade\n" +
                "JOIN robota_brigade ON robota_brigade.brigade_id = brigade.id\n" +
                "JOIN robota ON robota_brigade.robota_id = robota.id\n" +
                "JOIN crash ON robota.crash_id = crash.id\n" +
                "WHERE crash.adress = " + "'" + adress + "'";

        PreparedStatement p = getDbConnection().prepareStatement(find);

        resset = p.executeQuery();

        String brigade = "Не знайдена";

        while (resset.next()) {
            brigade = resset.getString(1);
        }

        String answer = brigade + " бригада";

        return answer;
    }
// Метод для знаходження яка бригада працювала на виїзній роботі за датою роботи
    public String findbrigadebydate(String date) throws SQLException, ClassNotFoundException {
        ResultSet resset = null;

        String find = "SELECT brigade.id FROM brigade\n" +
                "JOIN robota_brigade ON robota_brigade.brigade_id = brigade.id\n" +
                "JOIN robota ON robota_brigade.robota_id = robota.id\n" +
                "JOIN crash ON robota.crash_id = crash.id\n" +
                "WHERE robota.robota_date = " + "'" + date + "'";

        PreparedStatement p = getDbConnection().prepareStatement(find);

        resset = p.executeQuery();

        String brigade = "Не знайдена";

        while (resset.next()) {
            brigade = resset.getString(1);
        }

        String answer = brigade + " бригада";

        return answer;
    }
// Метод для знаходження працівника за фамілією
    public String findworker(String sur) throws SQLException, ClassNotFoundException {
        String selectpos = "SELECT posada.name FROM posada\n" +
                "WHERE posada.id in (\n" +
                "\tSELECT worker.posada FROM worker \n" +
                "    WHERE worker.surname = " + "'" + sur + "'" + ")";

        PreparedStatement pos = getDbConnection().prepareStatement(selectpos);

        ResultSet rpos = pos.executeQuery();

        String selectsal = "SELECT posada.plata FROM posada\n" +
                "WHERE posada.id IN (\n" +
                "\tSELECT worker.posada FROM worker\n" +
                "    WHERE worker.surname = " + "'" + sur + "'" + ")";
        PreparedStatement psal = getDbConnection().prepareStatement(selectsal);
        ResultSet rsal = psal.executeQuery();


        ResultSet resset = null;
        String find = "SELECT * FROM worker WHERE surname = ?";
        PreparedStatement p = getDbConnection().prepareStatement(find);
        p.setString(1, sur);

        resset = p.executeQuery();

        String name = "Нема інформації";
        String surname = "";
        String third = "";
        String work_start = "Нема іонформації";
        String posada = "Нема інформації";
        while(rpos.next()) {
            posada = rpos.getString(1);
        }
        String salary = "Нема інформації";
        while(rsal.next()) {
            salary = rsal.getString(1);
        }
        String brigade = "Нема інформації";

        while(resset.next()) {
            name = resset.getString(2);
            surname = resset.getString(3);
            third = resset.getString(4);
            work_start = resset.getString(5);
            brigade = resset.getString(7);
        }

        String result = "ПІБ: " + surname + " " + name + " " + third + "\n" +
                "Працює з: " + work_start + "\n" +
                "Посада: " + posada + "\n" +
                "Зарплатня: " + salary + "\n" +
                "Бригада: " + brigade;

        return result;
    }
// Метод для додавання працівника до системи
    public void addworker(String name, String surname, String third, String posada, String workstart) throws SQLException, ClassNotFoundException {
        String pos = "SELECT posada.id FROM posada\n" +
                "WHERE posada.name = " + "'" + posada + "'";
        PreparedStatement ppos = getDbConnection().prepareStatement(pos);
        ResultSet rpos = ppos.executeQuery();
        String posadaid = "";

        while(rpos.next()) {
            posadaid = rpos.getString(1);
        }

        String insert = "INSERT INTO worker(name, surname, third_name, work_start, posada)" +
                "VALUES (?,?,?,?,?)";

        PreparedStatement p = getDbConnection().prepareStatement(insert);

        p.setString(1, name);
        p.setString(2, surname);
        p.setString(3, third);
        p.setString(4, workstart);
        p.setString(5, posadaid);

        p.executeUpdate();
    }
// Метод для видалення працівника із системи
    public void deleteworker(String surname) throws SQLException, ClassNotFoundException {
        String delete = "DELETE FROM worker WHERE worker.surname = " + "'" + surname + "'";

        PreparedStatement p = getDbConnection().prepareStatement(delete);

        p.executeUpdate();
    }


    // Усі запити курсової роботи

    // 	Яку посаду займає кожен працівник?
    public ResultSet whatposada() throws SQLException, ClassNotFoundException {
        String insert = "SELECT worker.surname, posada.name FROM worker \n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "ORDER BY posada.name\n";

        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
//  Яке авто закріплене за кожною бригадою
    public ResultSet whatavto() throws SQLException, ClassNotFoundException {
        String insert = "SELECT brigade.id, avto.type FROM brigade \n" +
                "JOIN avto on brigade.avto = avto.id\n" +
                "ORDER BY brigade.id\n";

        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Хто з абонентів заплатив за контракт від 3 до 4 тисяч грн
    public ResultSet whopaidmorethan(int a) throws SQLException, ClassNotFoundException {
        String insert = "SELECT abonent.surname, contract.price FROM abonent \n" +
                "JOIN contract ON contract.abonent = abonent.id\n" +
                "WHERE contract.price LIKE " + "'" + a + "'";

        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Хто з робітників почав працювати в 2017 році?
    public ResultSet whostarted(int a) throws SQLException, ClassNotFoundException {
        String insert = "SELECT worker.surname, worker.work_start FROM worker\n" +
                "WHERE worker.work_start LIKE \n " + "'" + a + "'";

        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Виїзні роботи якого типу проводились в проміжку з січня 2019 року до липня 2020 року?
    public ResultSet whatrobotasbetween(String date1, String date2) throws SQLException, ClassNotFoundException {
        String insert = "SELECT type, robota_date FROM robota \n" +
                "WHERE robota_date BETWEEN " + "'" + date1 + "'" + " AND " + "'" + date2 + "'";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Хто з працівників має заробітню плату від 4500 грн до 9000 грн?
    public ResultSet whopaidbetween(int a, int b) throws SQLException, ClassNotFoundException {
        String insert = "SELECT worker.surname, posada.plata FROM worker \n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "WHERE posada.plata BETWEEN " + "'" + a + "'" + " AND " + "'" + b + "'";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 1)	Скільки усього енергії витратили всі абоненти
    public ResultSet allenergy() throws SQLException, ClassNotFoundException {
        String insert = "SELECT SUM(abonent.energy_used) FROM abonent";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Яка середня заробітня плата в працівників електрослужби?
    public ResultSet averagesalary() throws SQLException, ClassNotFoundException {
        String insert = "SELECT AVG(posada.plata) FROM posada";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Скільки працівників належать до кожної професії?
    public ResultSet posadaworkers() throws SQLException, ClassNotFoundException {
        String insert = "SELECT COUNT(worker.id), posada.name FROM worker \n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "GROUP BY posada.name\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Скільки робіт кожного виду було проведено?
    public ResultSet everytyperobotas() throws SQLException, ClassNotFoundException {
        String insert = "SELECT COUNT(robota.id), robota.type FROM robota\n" +
                "GROUP BY robota.type\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	В кого з працівників заробітня плата більша за 5000грн?
    public ResultSet anypaidmore(int a) throws SQLException, ClassNotFoundException {
        String insert = "SELECT worker.* FROM worker\n" +
                "\n" +
                "WHERE worker.id = ANY \n" +
                "(SELECT worker.id FROM worker \n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "WHERE posada.plata > " + "'" + a + "'" + ")";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	У кого з працівників заробітня плата більша ніж у монтажників?
    public ResultSet paidmorethanposada(String posada) throws SQLException, ClassNotFoundException {
        String insert = "SELECT worker.surname FROM worker \n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "WHERE posada.plata > ALL (\n" +
                "\tSELECT posada.plata FROM posada \n" +
                "    WHERE posada.name = " + "'" + posada + "'" +
                ")\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	До якої бригади належить монтажник Марченко?
    public ResultSet whatbrigade(String surname) throws SQLException, ClassNotFoundException {
        String insert = "SELECT brigade.id FROM brigade\n" +
                "WHERE brigade.id IN (\n" +
                "    SELECT worker.brigade_id FROM worker \n" +
                "    WHERE worker.surname = " + "'" + surname + "'" +
                ")\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Скільки заробляє працівник з id 3?
    public ResultSet howmuchpaid(int id) throws SQLException, ClassNotFoundException {
        String insert = "SELECT posada.plata FROM posada \n" +
                "WHERE posada.id IN (\n" +
                "\tSELECT worker.posada FROM worker \n" +
                "    WHERE worker.id = " + "'" + id + "'" +
                ")\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	У яких бригадах авто – не вантажне?
    public ResultSet avtonottype(String type) throws SQLException, ClassNotFoundException {
        String insert = "SELECT brigade.id FROM brigade \n" +
                "LEFT JOIN avto ON brigade.avto = avto.id\n" +
                "WHERE avto.type NOT LIKE " + "'" + type + "'";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
//	До якої бригади не прикріплене авто?
    public ResultSet brigadewithoutavto() throws SQLException, ClassNotFoundException {
        String insert = "SELECT brigade.id FROM brigade\n" +
                "LEFT JOIN avto ON brigade.avto = avto.id\n" +
                "WHERE avto.id IS NULL\n'";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Список працівників з з коментарями щодо рівню зарплатні
    public ResultSet workerslist() throws SQLException, ClassNotFoundException {
        String insert = "SELECT \"Вища заробітня плата\" as coment, worker.* FROM worker\n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "WHERE posada.plata > \"7000\"\n" +
                "\n" +
                "UNION\n" +
                "SELECT \"Середня заробітня плата\" as coment, worker.* FROM worker\n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "WHERE posada.plata BETWEEN \"5000\" AND \"7000\"\n" +
                "\n" +
                "UNION\n" +
                "SELECT \"Нижча заробітня плата\" as coment, worker.* FROM worker\n" +
                "JOIN posada ON worker.posada = posada.id\n" +
                "WHERE posada.plata BETWEEN \"4000\" AND \"5000\"\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Список абонентів з коментарями щодо кількості витраченої енергії
    public ResultSet abonentslist() throws SQLException, ClassNotFoundException {
        String insert = "SELECT \"Велике споживання енергії\" as coment, abonent.surname, abonent.id FROM abonent\n" +
                "WHERE energy_used > \"400\"\n" +
                "\n" +
                "UNION \n" +
                "SELECT \"Середнє споживання енергії\" as coment, abonent.surname, abonent.id FROM abonent\n" +
                "WHERE energy_used BETWEEN \"200\" AND \"400\"\n" +
                "\n" +
                "UNION \n" +
                "SELECT \"Низьке споживання енергії\" as coment, abonent.surname, abonent.id FROM abonent\n" +
                "WHERE energy_used < \"200\"\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        ResultSet r = p.executeQuery();
        return r;
    }
// 	Для бригади з id 3 замінити тип авто на легковий
    public void changeavtotype(int id) throws SQLException, ClassNotFoundException {
        String insert = "UPDATE avto SET avto.type = \"Легкова\"\n" +
                "WHERE avto.id IN (\n" +
                "    SELECT * FROM(\n" +
                "\tSELECT avto.id FROM avto \n" +
                "    JOIN brigade ON avto.brigade = brigade.id\n" +
                "    WHERE brigade.id = " + "'" + id + "'" +
                ")a)\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        p.executeUpdate();

    }
//	Замінити плату на 1000 грн для працівника з прізвищем Шевченко
    public void changesalary(String surname) throws SQLException, ClassNotFoundException {
        String insert = "UPDATE posada SET posada.plata = \"1000\"\n" +
                "WHERE posada.id IN (\n" +
                "    SELECT * FROM (\n" +
                "\tSELECT posada.id FROM posada\n" +
                "    JOIN worker ON posada.id = worker.posada\n" +
                "    WHERE worker.surname = " + "'" + surname + "'" +
                ")b)\n";
        PreparedStatement p = getDbConnection().prepareStatement(insert);
        p.executeUpdate();

    }

}


