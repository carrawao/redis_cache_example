package com.redis.rosebowl.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rose_bowl")
public class RoseBowl {
	@Id
	@Column
	private Long date;

	@Column
	private String winner;

	@Column
	private String opponent;

	public RoseBowl() {}

	public RoseBowl(Long date, String winner, String opponent) {
		this.date = date;
		this.winner = winner;
		this.opponent = opponent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RoseBowl rosebowl = (RoseBowl) o;
		return Objects.equals(date, rosebowl.date) &&
			Objects.equals(winner, rosebowl.winner) &&
			Objects.equals(opponent, rosebowl.opponent);
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, winner, opponent);
	}

	public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    @Override
	public String toString() {
		return "{'date': '" + date + '\'' +
			", 'winner': '" + winner + '\'' +
			", 'opponent': '" + opponent + '\'' +
			"}";
	}
}    