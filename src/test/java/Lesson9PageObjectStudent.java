import lombok.Getter;

import java.util.List;

@Getter
public class Lesson9PageObjectStudent {
   String name;
   String secondName;
   String studyGroup;
   Integer streamNumber;
   List<Integer> marksList;
   List <String> passedBlocks;

   public static Lesson9PageObjectStudentBuilder builder() {
       return new Lesson9PageObjectStudentBuilder();
   }

}
