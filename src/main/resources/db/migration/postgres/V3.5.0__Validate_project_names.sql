UPDATE "PROJECT" SET "NAME" = '(Undefined)' WHERE "NAME" IS NULL OR LTRIM(RTRIM("NAME")) = '''';