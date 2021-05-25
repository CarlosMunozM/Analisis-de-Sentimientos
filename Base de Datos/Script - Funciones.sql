-- Carlos Muñoz

--1

CREATE OR REPLACE FUNCTION login(
    IN usuario character varying,
    IN clave character varying,
    OUT id_persona integer,
    OUT id_tipousuario integer,
    OUT nombres character varying,
    OUT apellidos character varying)
  RETURNS SETOF record AS
$BODY$
BEGIN
	return query
	SELECT P.id_persona, P.id_tipousuario, P.nombres, P.apellidos
	FROM persona P
	WHERE P.usuario = $1 AND P.clave = MD5($2) AND estado = '1';
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

--2


CREATE OR REPLACE FUNCTION registrarjugador(
    nombres character varying,
    apellidos character varying,
    telefono character varying,
    fecha_nacimiento character varying,
    usuario character varying,
    clave character varying,
    genero character)
  RETURNS void AS
$BODY$
DECLARE
    id integer;
BEGIN
	IF ((SELECT MAX(id_persona) FROM persona) IS null ) THEN

	   INSERT INTO persona (id_persona, nombres, apellidos, telefono, fecha_nacimiento, usuario, clave, genero, id_tipousuario, estado)
	   VALUES (1, $1, $2, $3, $4, $5, MD5($6), $7, 2, 1);
	   
	ELSE
   
	   id:= (SELECT MAX(id_persona)+1 FROM persona);
	    
	   INSERT INTO persona (id_persona, nombres, apellidos, telefono, fecha_nacimiento, usuario, clave, genero, id_tipousuario, estado)
	   VALUES (id, $1, $2, $3, $4, $5, MD5($6), $7, 2, 1);
        
	END IF;	
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



--3

CREATE OR REPLACE FUNCTION buscarusuario(nombreusuario character varying)
  RETURNS character varying AS
$BODY$
declare
	users character varying;
begin
	IF ((SELECT usuario FROM persona WHERE usuario = $1 GROUP BY usuario) IS null ) THEN
		users:='no';
	ELSE
		
		
		IF ((SELECT usuario FROM persona WHERE usuario = $1 GROUP BY usuario)IS null ) THEN
			users:='no';
		ELSE
			users:='si';
		END IF;
	END IF;

	RETURN users;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

--4

CREATE OR REPLACE FUNCTION rutasimagenes(
    OUT id_imagen integer,
    OUT ruta character varying)
  RETURNS SETOF record AS
$BODY$
begin
	return query
	SELECT I.id_imagen, I.ruta from imagenes I ;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

--5

CREATE OR REPLACE FUNCTION registrarjuego(
    id_persona integer,
    resultado character varying)
  RETURNS INTEGER AS
$BODY$
DECLARE
    id integer;
BEGIN
	IF ((SELECT MAX(id_juego) FROM juego) IS null ) THEN

	   INSERT INTO juego (id_juego, resultado, fecha, id_persona, hora) 
	   VALUES (1, $2, current_date, $1, current_time);

	   RETURN 1;
	ELSE
	   id:= (SELECT MAX(id_juego)+1 FROM juego);
	    
	   INSERT INTO juego (id_juego, resultado, fecha, id_persona, hora) 
	   VALUES (id, $2, current_date, $1, current_time);        

	   RETURN id;
	END IF;	
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

--6

CREATE OR REPLACE FUNCTION registrarjuegoimagen(
    id_juego integer,
    id_imagen integer,
    opinion character varying)
  RETURNS void AS
$BODY$
DECLARE
    id integer;
BEGIN
	IF ((SELECT MAX(id_juegoimagen) FROM juego_imagen) IS null ) THEN

	   INSERT INTO juego_imagen (id_juego, id_imagen, opinion, id_juegoimagen) 
	   VALUES ($1,$2, $3, 1);
	  
	ELSE
   
	   id:= (SELECT MAX(id_juegoimagen)+1 FROM juego_imagen);
	    
	   INSERT INTO juego_imagen (id_juego, id_imagen, opinion, id_juegoimagen) 
	   VALUES ($1,$2, $3, id);
	   
	END IF;	
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

--7

CREATE OR REPLACE FUNCTION obteneranalisis(
    id_juego integer)
  RETURNS character varying AS
$BODY$
DECLARE
    salida character varying;
begin
	salida:= (SELECT J.resultado FROM juego J WHERE J.id_juego = $1);

	return salida;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100

--8

CREATE OR REPLACE FUNCTION mostrar_jugadores(
    OUT idpersona integer,
    OUT nombres character varying,
    OUT apellidos character varying,
    OUT telefono character varying,
    OUT fecha_nacimiento  character varying,
    OUT usuario character varying,
    OUT genero character)
  RETURNS SETOF record AS
$BODY$
begin
	return query
	SELECT P.id_persona, P.nombres, P.apellidos, P.telefono, P.fecha_nacimiento, P.usuario, P.genero
	FROM persona P
	WHERE id_tipousuario = 2 AND estado = '1'
	ORDER BY P.Apellidos;
end
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

--9

CREATE OR REPLACE FUNCTION eliminar_jugador(
    id_persona integer)
  RETURNS void AS
$BODY$

BEGIN

	UPDATE persona P SET estado = 0 WHERE P.id_persona = $1;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

--10

CREATE OR REPLACE FUNCTION modificar_jugador(
    id_persona integer,
    nombres character varying,
    apellidos character varying,
    telefono character varying,
    fecha_nacimiento character varying,
    genero character varying)
  RETURNS void AS
$BODY$
BEGIN
	UPDATE persona P
	SET nombres = $2, apellidos = $3, telefono = $4, fecha_nacimiento = $5, genero = $6
	WHERE P.id_persona = $1; 
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

select modificar_jugador(5,'Francisco Steven','Rodr´guez Pérez', '0951862531', '1997-01-12', 'M')

