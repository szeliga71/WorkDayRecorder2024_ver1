package pl.wp.workdayrecorder2024_ver1.service;

    import org.springframework.data.jpa.domain.Specification;
import pl.wp.workdayrecorder2024_ver1.model.WorkDay;

    public class WorkDaySpecifications {

        public static Specification<WorkDay> hasPersonalId(String personalId) {
            return (root, query, cb) -> {
                if (personalId == null || personalId.isEmpty()) {
                    return null;  // Ignoruj, jeśli personalId nie zostało podane
                }
                return cb.equal(root.get("personalId"), personalId);
            };
        }

        public static Specification<WorkDay> hasDayOfWeek(String dayOfWeek) {
            return (root, query, cb) -> {
                if (dayOfWeek == null || dayOfWeek.isEmpty()) {
                    return null;  // Ignoruj, jeśli dayOfWeek nie zostało podane
                }
                return cb.equal(root.get("dayOfWeek"), dayOfWeek);
            };
        }

        public static Specification<WorkDay> hasKW(Integer KW) {
            return (root, query, cb) -> {
                if (KW == null) {
                    return null;  // Ignoruj, jeśli KW nie zostało podane
                }
                return cb.equal(root.get("KW"), KW);
            };
        }
    }


