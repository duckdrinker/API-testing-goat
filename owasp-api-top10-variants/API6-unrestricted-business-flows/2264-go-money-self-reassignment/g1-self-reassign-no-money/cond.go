package main

type Cond map[string]interface{}

func (c Cond) And(o Cond) Cond {
	for k, v := range o {
		c[k] = v
	}
	return c
}

func buildCond(repoID, ownerID uint) Cond {
	cond := Cond{}
	cond = cond.And(Cond{"repo_id": repoID})
	cond = cond.And(Cond{"owner_id": ownerID})
	cond = cond.And(Cond{"is_deleted": false})
	return cond
}
