package org.systemexception.adtrap.logarchiver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.systemexception.adtrap.logarchiver.model.DnsLogLine;

import java.util.List;

/**
 * @author leo
 * @date 07/10/2016 18:34
 */
@Repository
public interface DnsLogLineRepository extends JpaRepository<DnsLogLine, Long> {

	/**
	 * Save data to the database
	 *
	 * @param dnsLogLine the data to be saved
	 * @return the data saved
	 */
	DnsLogLine save(DnsLogLine dnsLogLine);

	@Query(value = "select count(dns) from DnsLogLine dns")
	int countAll();

	/**
	 * Clean up database
	 *
	 * @param date
	 * @return
	 */
	@Modifying
	@Query("delete from DnsLogLine dns where dns.date < :date")
	void cleanUp(@Param("date") Long date);

}
