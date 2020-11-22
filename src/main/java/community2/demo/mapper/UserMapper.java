package community2.demo.mapper;


import community2.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id, token,gmt_create,gmt_modified, bio) " +
            "values (#{name},#{account_id},#{token},#{gmt_create},#{gmt_modified}, #{bio})")
   void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
